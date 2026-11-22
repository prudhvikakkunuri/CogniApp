#!/usr/bin/env bash
set -euo pipefail

# CogniApp iOS & iPadOS .ipa Packaging Script
# Builds and packages CogniApp into a sideloadable .ipa file
# Works locally on macOS with Xcode, or inside CI/CD environments.
#
# Usage:
#   ./build_ipa.sh         # Archive and package IPA
#   ./build_ipa.sh --test  # Run unit tests before archiving

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="${SCRIPT_DIR}/CogniApp"
BUILD_DIR="${PROJECT_DIR}/build"
ARCHIVE_PATH="${BUILD_DIR}/CogniApp.xcarchive"
OUTPUT_DIR="${SCRIPT_DIR}/output"
IPA_PATH="${OUTPUT_DIR}/CogniApp.ipa"

RUN_TESTS=false
if [[ "${1:-}" == "--test" ]]; then
    RUN_TESTS=true
fi

echo "========================================="
echo "   Building CogniApp iOS & iPadOS IPA    "
echo "========================================="

# Clean previous build artifacts
rm -rf "${BUILD_DIR}" "${OUTPUT_DIR}"
mkdir -p "${BUILD_DIR}" "${OUTPUT_DIR}"

cd "${PROJECT_DIR}"

# If XcodeGen is available, regenerate xcodeproj
if command -v xcodegen &> /dev/null && [ -f "project.yml" ]; then
    echo "⚙️  Regenerating Xcode project with XcodeGen..."
    xcodegen generate
fi

if [ "$RUN_TESTS" = true ]; then
    echo "🧪 Running iOS unit tests..."
    set -o pipefail
    xcodebuild test \
        -project CogniApp.xcodeproj \
        -scheme CogniApp \
        -destination "platform=iOS Simulator,name=iPhone 15,OS=latest" \
        CODE_SIGNING_ALLOWED=NO \
        CODE_SIGNING_REQUIRED=NO \
        CODE_SIGN_IDENTITY="" \
        | (command -v xcpretty &> /dev/null && xcpretty || cat)
fi

echo "📦 Archiving CogniApp with xcodebuild..."
xcodebuild archive \
    -project CogniApp.xcodeproj \
    -scheme CogniApp \
    -configuration Release \
    -destination "generic/platform=iOS" \
    -archivePath "${ARCHIVE_PATH}" \
    CODE_SIGNING_ALLOWED=NO \
    CODE_SIGNING_REQUIRED=NO \
    CODE_SIGN_IDENTITY="" \
    | (command -v xcpretty &> /dev/null && xcpretty || cat)

# Verify archive creation
if [ ! -d "${ARCHIVE_PATH}" ]; then
    echo "❌ Xcode archive failed to generate at ${ARCHIVE_PATH}"
    exit 1
fi

echo "📦 Packaging IPA for sideloading (AltStore / SideStore / Sideloadly / Xcode)..."
APP_PATH="${ARCHIVE_PATH}/Products/Applications/CogniApp.app"

if [ ! -d "${APP_PATH}" ]; then
    echo "❌ Application bundle not found at ${APP_PATH}"
    exit 1
fi

PAYLOAD_DIR="${BUILD_DIR}/Payload"
mkdir -p "${PAYLOAD_DIR}"
cp -R "${APP_PATH}" "${PAYLOAD_DIR}/"

cd "${BUILD_DIR}"
zip -qr "${IPA_PATH}" "Payload"

echo "========================================="
echo "✅ Successfully packaged CogniApp.ipa!"
echo "📍 Output location: ${IPA_PATH}"
echo "📊 IPA file size: $(du -h "${IPA_PATH}" | cut -f1)"
echo "========================================="
