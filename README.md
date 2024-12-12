Let's Play: 아동 역할놀이 AI 챗봇 앱
Let's Play는 아동을 위한 대화형 역할놀이 안드로이드 애플리케이션입니다. AI 챗봇과의 실시간 음성 상호작용을 통해 아이들의 언어 발달과 창의력 향상을 돕습니다.
주요 기능
AI 챗봇과의 역할놀이: 음성 인식과 AI 응답을 통한 실시간 대화형 역할놀이
음성 녹음 및 처리: 사용자의 음성을 녹음하고 서버로 전송하여 AI 응답 생성
실시간 채팅 인터페이스: RecyclerView를 사용한 대화 내용 표시
애니메이션 효과: 녹음 중 주파수 애니메이션으로 시각적 피드백 제공
권한 관리: 오디오 녹음 권한 요청 및 처리
메뉴 시스템: 다양한 옵션과 설정을 위한 메뉴 인터페이스
주요 액티비티
MainActivity: 앱의 시작점, 시작 버튼을 통해 GreetingActivity로 이동
GreetingActivity: 인사 화면 표시 후 PrivacyPolicyActivity로 자동 전환
PrivacyPolicyActivity: 개인정보 처리방침 표시
PermissionAppActivity: 앱 권한 요청 및 관리
RewardsMainPageActivity: 보상 시스템 메인 페이지
RoleplayingBackgroundActivity: 핵심 역할놀이 기능 구현
MenuActivity: 앱 설정 및 추가 기능 접근을 위한 메뉴
EndingActivity: 앱 종료 화면
SplashEndingActivity: 종료 애니메이션 표시 후 앱 완전 종료
기술 스택
언어: Java
프레임워크: Android SDK
네트워크: WebSocket (서버 통신용)
음성 처리: WAVRecorder (커스텀 녹음 클래스)
설치 및 실행
프로젝트를 Android Studio에서 열기
필요한 SDK 및 라이브러리 설치
에뮬레이터 또는 실제 기기에서 앱 실행
권한
RECORD_AUDIO: 음성 녹음용
INTERNET: 서버 통신용
READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE: 음성 파일 저장용
주의사항
앱 사용을 위해 오디오 녹음 권한이 필요합니다.
안정적인 인터넷 연결이 필요합니다.
서버 URL 설정이 필요합니다 (RoleplayingBackgroundActivity의 serverURL_wss 변수).
향후 개선 사항
다국어 지원 추가
오프라인 모드 구현
사용자 프로필 및 진행 상황 저장 기능
라이선스
이 프로젝트는 MIT 라이선스 하에 있습니다. 자세한 내용은 LICENSE 파일을 참조하세요.
