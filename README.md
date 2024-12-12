# Let's Play: AI Chatbot Role-Playing App for Children

Let's Play is an Android application designed for interactive role-playing experiences for children. It utilizes real-time voice interactions with an AI chatbot to enhance language development and creativity in kids.

## Key Features

- **AI Chatbot Role-Playing**: Real-time conversational role-play through voice recognition and AI responses
- **Voice Recording and Processing**: Records user's voice and sends it to the server for AI response generation
- **Real-time Chat Interface**: Displays conversation content using RecyclerView
- **Animation Effects**: Provides visual feedback with frequency animation during recording
- **Permission Management**: Requests and handles audio recording permissions
- **Menu System**: Menu interface for various options and settings

## Main Activities

1. **MainActivity**: Entry point of the app
2. **GreetingActivity**: Displays greeting screen
3. **PrivacyPolicyActivity**: Shows privacy policy
4. **PermissionAppActivity**: Manages app permissions
5. **RewardsMainPageActivity**: Main page for the reward system
6. **RoleplayingBackgroundActivity**: Implements core role-playing functionality
7. **MenuActivity**: Provides access to app settings and additional features
8. **EndingActivity**: App closing screen
9. **SplashEndingActivity**: Displays closing animation before fully exiting the app

## Technology Stack

- Language: Java
- Framework: Android SDK
- Networking: WebSocket (for server communication)
- Audio Processing: WAVRecorder (custom recording class)

## Installation and Running

1. Open the project in Android Studio
2. Install necessary SDKs and libraries
3. Run the app on an emulator or physical device

## Permissions

- `RECORD_AUDIO`: For voice recording
- `INTERNET`: For server communication
- `READ_EXTERNAL_STORAGE`, `WRITE_EXTERNAL_STORAGE`: For saving voice files

## Notes

- Audio recording permission is required for app usage
- Stable internet connection is necessary
- Server URL needs to be set (in `serverURL_wss` variable of RoleplayingBackgroundActivity)

## Future Improvements

- Add multi-language support
- Implement offline mode
- Add user profile and progress saving features

## License

This project is under the MIT License. See the LICENSE file for details.

Citations:
[1] https://ppl-ai-file-upload.s3.amazonaws.com/web/direct-files/43969710/0a0d7c77-fe6d-44ec-9f37-3d50f90dc93d/paste.txt
