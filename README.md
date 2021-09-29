# BBS
> 리뷰를 카테고리 별로 모아보는 게시판

## 사용 언어
  * JAVA
## 사용 기술
  * Spring Boot 2.5.2
  * Oracle 19c
  * Redis (Windows local 설치)
  * Thymeleaf
  * jQuery
  * WYSIWYG Editor : summernote v0.8.18
  * Toast UI Grid v4.19.1 << 프로젝트 다운로드 후 /resources/static 하위에 tui-grid 폴더 복사 붙여넣기

### Toast UI Grid 적용 방법
1. npm 다운로드<br/>

2. 원하는 폴더에 npm init<br/>

3. 윈도우 기준 cmd에 **npm install tui-grid** 입력<br/>

4. 해당 디렉토리에 /node_modules/tui-grid 복사<br/>

5. Spring Boot application에서 /resources/static 에 복사한 tui-grid 붙여넣기<br/>
![image](https://user-images.githubusercontent.com/55985137/135185519-db000921-a7f0-4a3b-bd30-ff88e241668e.png)

6. grid를 적용하고 싶은 html의 <head>에 아래 코드를 적용<br/>
 ```<link rel="stylesheet" href="/tui-grid/tui-grid.css">```
 
7. body 태그가 끝나기 전에 아래와 같이 script 적용<br/>
 ```<script src="/tui-grid/tui-grid.js"></script>```
