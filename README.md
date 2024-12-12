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






# Server for Children's Role-Play Application

This project implements a server to support a role-playing application designed for children's educational purposes. The server integrates advanced conversational AI technology using a Text-to-Speech (TTS) → Language Model (LLM) → Speech-to-Text (STT) pipeline, providing real-time interactions that enhance early childhood education and language development.

---

## Features

### Conversational AI
- Implements natural, engaging, and context-aware conversations to support role-playing scenarios.

### Stream Responses
- **LLM Streaming**: Generates and streams text responses incrementally to reduce inference latency.
- **TTS Streaming**: Converts text responses into audio in real-time for faster feedback.

### Integrated Pipeline
- **TTS**: Fine-tuned Whisper-small model trained on datasets from AIHub:
  - 한국어 아동 음성 데이터
  - 자유대화 음성 (소아 남여, 유아 등 혼합)
- **LLM**: Utilizes OpenAI’s gpt-4o-mini for conversational logic.
- **STT**: Uses OpenAI’s tts-1 for accurate speech recognition.

---

## Architecture

The application is built using:
- **FastAPI**: A high-performance web framework for building and serving the API endpoints.
- **Docker**: Containerizes the application for consistent and efficient deployment.
- **Google Cloud Run**: Deploys the server in a scalable, managed environment for cost-effective hosting.

---

## Prerequisites

Before deploying or running the application, ensure you have the following:

- Python 3.8+
- Docker and Docker Compose installed
- Google Cloud Platform (GCP) account with Cloud Run enabled
- OpenAI API key stored as an environment variable: `OPENAI_API_KEY`

---

## Installation

### 1. Clone the Repository
```bash
git clone https://github.com/Jaeho-Jung/Capstone2024_server.git
cd Capstone2024_server
```

### 2. Install Dependencies
Use pip to install Python dependencies:
```bash
pip install -r requirements.txt
```

### 3. Configure Environment Variables
Create a `.env` file in the project root with the following content:
```
OPENAI_API_KEY=your_openai_api_key
```

### 4. Run Locally (Optional)
To run the application locally for development:
```bash
uvicorn main:app --reload --host 0.0.0.0 --port 8080
```

---

## Docker Setup

### 1. Build the Docker Image
```bash
docker build -t roleplay-server .
```

### 2. Run the Docker Container
```bash
docker run -d -p 8080:8080 --env-file .env roleplay-server
```

---

## Deployment

### 1. Push Docker Image to Google Container Registry (GCR)
```bash
gcloud auth configure-docker
docker tag roleplay-server gcr.io/<your-project-id>/roleplay-server
docker push gcr.io/<your-project-id>/roleplay-server
```

### 2. Deploy to Google Cloud Run
```bash
gcloud run deploy roleplay-server \
    --image gcr.io/<your-project-id>/roleplay-server \
    --platform managed \
    --region <your-region> \
    --allow-unauthenticated \
    --set-env-vars OPENAI_API_KEY=your_openai_api_key
```

---

## API Endpoints

### `/roleplay/reset_conversation` (POST)
Resets the conversation context to start a new dialogue.

### `/roleplay/stream` (WebSocket)
Handles streaming interactions in the following sequence:
1. Receives audio input from the client.
2. Processes the input using the STT pipeline.
3. Generates a response using the LLM.
4. Converts the response to audio using the TTS.
5. Streams the audio back to the client in real time.

---

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository.
2. Create a new branch:
   ```bash
   git checkout -b feature/your-feature-name
   ```
3. Commit your changes:
   ```bash
   git commit -m 'Add some feature'
   ```
4. Push to the branch:
   ```bash
   git push origin feature/your-feature-name
   ```
5. Open a Pull Request.

---

## License

This project is licensed under the MIT License. See the `LICENSE` file for more information.

---

## Acknowledgments

- **FastAPI**: For powering the API.
- **OpenAI**: For providing the gpt-4o-mini and tts-1 models.
- **AIHub**: For datasets used in fine-tuning the TTS model.
- **Google Cloud Platform**: For scalable hosting through Cloud Run.


