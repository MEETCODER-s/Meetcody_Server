# 💙 Meetcody-Server 💙
<br>

## 🌱프로젝트 소개
밋코디는 여러사람이 약속 장소와 시간을 정할 때 **출발지 입력**과 **구글 캘린더 계정** 만으로 약속장소와 시간 모두를 정할 수 있는 서비스이다. 밋코디 에서는 약속 장소 추천과 약속 시간 추천이라는 두가지 기능을 제공한다.

약속 장소 추천
- Dijkstra 알고리즘을 사용하여 출발지와 가까운 지하철 역들을 시작점으로 잡아 역간 이동시간을 고려한 이동 시간이 가장 가까운 장소를 계산하여 추천

약속 시간 추천
- 초대장을 수락한 사용자들의 구글 캘린더 속 일정을 api로 가져와 약속 주최자가 선택한 우선순위를 기준으로 추천

<br>
<br>

## 👩‍💻개발 담당 부분
| 담당자 | 구현 기능 |
|:------:|:------:|
| 송인아 | User, Social Login |
| 황유진 | Recommend Service, User |

<br><br>

## ⭐️PREVIEW

|구글 소셜 로그인 | 메인 페이지 | 초대 수락 페이지 | 일정 상세 확인 페이지 |
|:------:|:------:|:------:|:------:|
| <img src="https://user-images.githubusercontent.com/53734935/168985728-496b8210-80af-42a9-9470-e130033d35f3.png" height="400"> | 약속 생성, 초대된 약속 확인 탭<br><img src="https://user-images.githubusercontent.com/53734935/168985906-04046097-3b94-4e43-b5ec-28a2a9a1ac9a.png" height="400"> | <img src="https://user-images.githubusercontent.com/53734935/168985936-c335cef4-475f-417f-a393-4084231b67d4.png" height="400"> | <img src="https://user-images.githubusercontent.com/53734935/168985982-f5e9685c-f566-4170-8c07-f7eb5158a090.png" height="400">|

| 메인 페이지 | 추천 장소, 추천 시간 선택 페이지 | 약속 생성 페이지 |
|:------:|:------:|:------:| 
| 일정 조회 탭<br> <img src="https://user-images.githubusercontent.com/53734935/168986051-9066d06e-1c07-4734-9e65-f0a11ef97adb.png" height="400"> | <img src="https://user-images.githubusercontent.com/53734935/168986014-56958a40-8c9c-45e6-9f93-4347b3079183.png" height="400"> | <img src="https://user-images.githubusercontent.com/53734935/168986051-9066d06e-1c07-4734-9e65-f0a11ef97adb.png"  height="400" /> |

<br>

### 시연영상

[![밋코디시연영상](https://img.youtube.com/vi/6CPnYNzTc3I/2.jpg)](https://youtu.be/6CPnYNzTc3I)