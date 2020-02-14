# BobAR_Server_2019

*이미지 파일 이름 형식
    * 리뷰 사진 파일 이름 형식
    0 000000 000000 00000000 : 사진분류(리뷰 : 0) 음식점번호(6자리) 유저번호(6자리) 리뷰번호(8자리)

    * 프로필 사진 파일 이름 형식
    0 000000 : 사진분류(프로필 : 1) 유저번호(6자리)

*요청 코드
My page 코드 : 0
초기화면 요청 코드 : 1
(간단 리뷰 코드 : 2)
상세 리뷰 코드 : 3
이미지 파일 요청 코드 : 4
해당 리뷰 상세 보기 : 5
검색 : 6
리뷰 작성 - 리뷰 이미지 파일 이름 생성 : 7
리뷰 작성 - 이미지 파일 전송 : 8
리뷰 작성 - 리뷰 데이터 전송 : 9
팔로워 : 10
팔로잉 : 11

*작업
1. My page
- 내가 쓴 리뷰, 팔로워, 팔로잉
    (1)My data
    client code : 0
    client request : client code%%user id%% (ex)0%%bobar%%

    server response : 
    nickname%%profile file name%%follower num%%following num%%review num%%
    review id%%restaurant name%%image file name%%
    review id%%restaurant name%%image file name%%
    review id%%restaurant name%%image file name%%
    ...
    ex)
    모먹지%%1000001%%5%%10%%2
    1%%코고미식당%%000000100000100000001%%
    2%%다리집%%000000200000100000002%%

    (2)사진 요청
    client code : 4
    client request : client code%%image file name%% (ex)4%%000000100000100000001%%

    server response : 
    먼저 image file header(파일 크기) 전송 후 body(이미지 파일) 전송

2. 초기화면(자기 주변의 모든 식당 뿌려주기)
- 음식점 고유 번호와 해당 음식점의 최근 리뷰 사진
    (1)음식점 고유번호와 리뷰사진 파일 이름 요청
    client code : 1
    client request : 1

    server response : 
    restaurant num%%
    restaurant id%%longitude%%%latitude%%altitude%%img file name%%
    restaurant id%%longitude%%%latitude%%altitude%%img file name%%
    restaurant id%%longitude%%%latitude%%altitude%%img file name%%..
    ex)4%%
    1%%123.15331%%35.14633%%0%%000000100000100000001%%
    3%%123.15331%%35.14633%%0%%000000300000400000002%%..

    (2)리뷰사진 요청
    client code : 4
    client request : client code%%image file name%% (ex)4%%000000100000100000001%%

    server response : 
    먼저 image file header(파일 크기) 전송 후 body(이미지 파일) 전송

--------------------------------간단 리뷰 제외-----------------------------------------------
3. 간단리뷰 : 식당 선택시 해당 식당 정보와 평균별점, 가장 최근에 등록된 리뷰를 보여줌
- 해당 음식점 정보와 가장 최근 리뷰
    client code : 2
    client request : client code%%restaurant id%% (ex)2%%3%%

    server response : restaurant name%%restaurant address%%phone num%%review text%%
    (ex) 모티집%%부산 부산진구 동천로85번길 22%%010-8836-9192%%분위기랑 친절도가 높은편이며 끝에 먹는 칼국수가 맛있음%%
----------------------------------------------------------------------------------------------

4. 상세리뷰 : 사진 누르면 바로 상세리뷰
- 해당 음식점 정보와 모든 리뷰
    (1) 음식점 정보와 리뷰 데이터 요청
    client code : 3
    client request : client code%%restaurant id%% (ex) 3%%2%%

    server response : 
    restaurant name%%restaurant avg point%%address%%phone number%%review num
    review id%%text%%img file name%%likes%%date%%
    review id%%text%%img file name%%likes%%date%%
    review id%%text%%img file name%%likes%%date%%
    ...
    ex)모티집%%4.3%%대연동 용소로82-1%%852-1234%%3
    1%%분위기랑 친절도가 높은편이며...%%000000200000100000001%%2%%2019-07-08%%
    8%%구성에 적혀있는대로 철판 가득...%%000000200000300000008%%2%%2019-07-08%%
    16%%맛있어요!!%%000000200000500000016%%2%%2019-07-08%%
    ...

    (2)리뷰 사진 요청
    client code : 4
    client request : client code%%image file name%% (ex)4%%000000200000100000001%%

    server response : 
    먼저 image file header(파일 크기) 전송 후 body(이미지 파일) 전송

5.해당리뷰 상세 보기 : 해당 리뷰의 상세 정보
-리뷰 데이터 요청
    client code : 5
    client request : client code%%review id%% (ex) 5%%1%%

    server response :
    user nickname%%taste Point%%clean Point%%kindness Point%%mood Point%%cost Point%%
    ex)모먹지%%3%%4%%4%%3%%5%%

6. 검색 : 검색단어를 포함한 음식점의 정보를 뿌려줌
- 리뷰에 검색 단어를 포함한 음식점들의 고유 번호와 해당 음식점의 최근 리뷰 사진
    (1)음식점 고유번호와 리뷰사진 파일 이름 요청
    client code : 6
    client request : client code%%search%%

    server response : 
    restaurant num%%
    restaurant id%%longitude%%%latitude%%altitude%%img file name%%
    restaurant id%%longitude%%%latitude%%altitude%%img file name%%
    restaurant id%%longitude%%%latitude%%altitude%%img file name%%..
    ex)4%%
    1%%123.15331%%35.14633%%0%%000000100000100000001%%
    3%%123.15331%%35.14633%%0%%000000300000400000002%%..

    (2)리뷰사진 요청
    client code : 4
    client request : client code%%image file name%% (ex)4%%000000100000100000001%%

    server response : 
    먼저 image file header(파일 크기) 전송 후 body(이미지 파일) 전송

7. 리뷰 작성
    (1) 유저 아이디와 음식점 아이디 전송
    client code : 7
    client request : client code%%user id%%restaurant id%%

    - 음식점 아이디 + 유저 아이디 + 리뷰 아이디를 조합해서 이미지 파일 이름 전송
    server response : image file name%%

    (2)이미지 파일 전송
    client code : 8
    client request : client code%%img file name(유저, 음식점 아이디, 리뷰 번호가 조합된)%%file length%%
    ex) 8%%000000200000300000008%%31243%%

    server response : 0(fail)/1(success)

    client send image to server

    (3)리뷰 데이터 전송
    client code : 9
    client request : client code%%img file name%%text%%tastePoint%%cleanPoint%%kindnessPoint%%moodPoint%%costPoint
    ex) 9%%00000200000300000008%%맛있어요%%5%%5%%5%%5%%

    server response : 0(fail)/1(success)

8. 팔로워
    (1) 유저 아이디 전송
    client code : 10
    client request : client code%%user id%%
    ex) 10%%bobar%%

    server response :
    follower num%%
    follower nickname%%profile file name%%review num%%is follow%%
    follower nickname%%profile file name%%review num%%is follow%%
    follower nickname%%profile file name%%review num%%is follow%%
    ...
    ex)
    4%%
    유저1%%1000000%%0%%true%%
    유저3%%1000000%%8%%true%%
    유저4%%1000000%%3%%true%%
    유저5%%1000000%%11%%true%%

9. 팔로잉
    (1) 유저 아이디 전송
    client code : 11
    client request : client code%%user id%%
    ex) 11%%bobar%%

    server response :
    following num%%
    following nickname%%profile file name%% review num%%
    following nickname%%profile file name%% review num%%
    following nickname%%profile file name%% review num%%
    ...
    ex)
    4%%
    유저1%%1000000%%0%%
    유저3%%1000000%%8%%
    유저4%%1000000%%3%%
    유저5%%1000000%%11%%
