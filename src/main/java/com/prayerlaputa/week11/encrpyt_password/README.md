# week 11 作业

请用你熟悉的编程语言写一个用户密码验证函数，Boolean checkPW（String 用户 ID，String 密码明文，String 密码密文），返回密码是否正确 boolean 值，密码加密算法使用你认为合适的加密算法。

## 思路  

>Spring Security 已经废弃了 MessageDigestPasswordEncoder，推荐使用 BCryptPasswordEncoder，也就是BCrypt来进行密码哈希。BCrypt 是为保存密码设计的算法，相比 MD5 要慢很多。

上述引自极客时间 java业务开发常见错误100例 “30 | 如何正确保存和传输敏感数据？”。

目前来讲，使用BCryptPasswordEncoder对密码加密更加安全。本周作业题目我就采用了这个方案。  

- 设计了两个接口（按道理注册、登录接口肯定需要使用POST, 但此处为方便测试，都设计成了GET接口）
    - /api/user/reg: 注册新用户，需传入用户名、密码
        - 示例：http://localhost:8090/api/user/reg?username=ccc&passwd=123456
    - /api/user/login: 用户登录，该接口将根据用户名查询用户数据库，将加密后的密码取出，与用户登录时输入的密码比较
        - 具体校验接口在UserService#checkPW(String username, String rawPassword, String encodedPassword) 方法中
        - 示例：http://localhost:8090/api/user/login?username=ccc&passwd=123454
       
       
## 程序运行说明  

1. 先在本地或是你方便的环境执行resources/db/user.sql，创建user_db数据库，并在该库中创建一个user_info表，保证用户名、加密后的密码  
2. 修改application.yml中的数据库连接，连到你的数据库，然后启动UserApplication
3. 按上述接口说明，先访问localhost:8090/api/user/reg接口，注册一个用户，然后再调用localhost:8090/api/user/login进行登录验证即可