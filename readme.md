# 스프링 부트와 aws로 혼자 구현하는 웹 서비스 
* 이 프로젝트는 '스프링 부트와 aws로 혼자 구현하는 웹 서비스' 책을 기반으로 구현했습니다.
* 해당 책은 예전에 나온 책이라 Spring boot3 버전으로 진행해보면서 일부 다른 점이 있습니다.
* 또한, 책에는 없지만 AWS 인프라 구축을 위해 테라폼을 사용했습니다. 
  * RDS는 AWS console에서 생성했으나, 서브넷과 라우팅은 테라폼으로 진행했습니다.

### 테라폼 사용법 
1. 테라폼 설치 
2. infra/terraform 디렉토리에서 init && plan && apply 명령어 수행
   * `.tfstate` 파일은 테라폼 리소스 파일(.tf)을 실행해 현재 aws 인프라 형상 상태를 기록하는 중요한 파일이므로 관리에 주의
```shell 
terraform init 
terraform plan 
terraform apply 
```