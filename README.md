# Speedy

**Speedy** 是基于**okhttp**封装的一款遵循**Resutful**风格的网络库。采用**apt**结合**okhttp**生成网络请求代码，避免运行时反射对性能的影响。设计上参考**retrofit**的**converter**和**adapter**实现，对不同格式的数据和返回类型进行扩展。

## 添加依赖

全局build.gradle文件

```groovy
buildscript {
    repositories {
        jcenter()
    }
}
```

使用speedy的module：

```groovy
// 替换成最新版本
implementation "com.drinker.speedy:annotation:1.0.2"
// 如果使用了converter/adapter，可不再依赖此库
implementation 'com.drinker.speedy:core:1.0.2'

annotationProcessor 'com.drinker.speedy:speedy-compile:1.0.2'
```

## 使用

支持`get/post/put/delete`等常用请求，对应相应的注解`@Get，@Post，@Put，@Delete`等。

### 构建service api 接口

```java
@Service
public interface IService {

    @Get("login/namespace?rand=12")
	Call<Value> getLogin(@Param String name, @Param String pwd);
    
}
```

### 请求携带参数

```java
@Get("login/namespace?rand=12")
Call<Value> getLogin(@Param String name, @Param String pwd);
```

### 请求携带map参数

```java
@Get("login/namespace")
Call<Value> getLogin(@ParamMap Map<String, String> maps);
```

### url替换

```java
@Get("{node}/login/namespace")
Call<Value> getLogin(@Path("node") String node, @Param String name, @Param String pwd);
```

### 请求携带Body

```java
@Post("login/namespace")
Call<Value> getLogin(@Body RequestBody body);
```

**注：**如果采用了`converter`,`body`可以为具体类型对象。

### 请求携带MultipartBody.Part

```java
@MultiPart("multipart/form-data; charset=utf-8")
@Post("login/namespace")
Call<Value> getMultipart(@Part MultipartBody.Part body);
```

### 请求携带List<MultipartBody.Part>

```java
@MultiPart("multipart/form-data; charset=utf-8")
@Post("login/namespace")
Call<Value> getMultiparts(@PartMap List<MultipartBody.Part> body);
```

### 上传表单

```java
@Form
@Post("/user/sign_up")
Call<Value> getSign(@Param("key") String keyParam, @Param("value") String value);
```

### 上传表单Map

```java
@FormMap
@Post("/user/sign_up")
Call<Value> getSign(@ParamMap Map<String, String> map);
```

## Converter

当前默认返回`okhttp3.ResponseBody`类型对象，目前支持下列`converter`:

1. **gson-converter:**`com.drinker.speedy.gsons-converter`
2. **jackson-converter:**`com.drinker.speedy.jackson-converter`
3. **moshi-converter:**`com.drinker.speedy.moshi-converter`
4. **proto-converter:**`com.drinker.speedy.proto-converter`

