## SOAP-сервис для управлениями пользователями

**Формулировка задания:**  
Необходимо разработать SOAP backend для веб-приложения. Основная задача бекенда - управление пользователями и их ролями. Описание модели данных:  
-	У пользователя может быть несколько ролей, одна роль может быть у нескольких пользователей. Например, Вася - Админ и Оператор, Петя - Оператор и Аналитик.  
-	Атрибуты пользователя - Имя, Логин (первичный ключ), Пароль (шифровать пароль в рамках тестового задания не требуется, это просто строка).  
-	Атрибуты роли – id (первичный ключ), Имя.  
 
Необходимо:  
1. Разработать SOAP-сервис и методы работы с данными. Сделать методы, которые будут:
  - Получать список пользователей из БД (без ролей)
  - Получать конкретного пользователя (с его ролями) из БД
  - Удалять пользователя в БД
  - Добавлять нового пользователя с ролями в БД. 
  - Редактировать существующего пользователя в БД. Если в запросе на редактирование передан массив ролей, система должна обновить список ролей пользователя в БД - новые привязки добавить, неактуальные привязки удалить.
2. На бекенде для методов добавления и редактирования должен производиться формато-логический контроль пришедших значений. Поля name, login, password - обязательные для заполнения, password содержит букву в заглавном регистре и цифру. 
  - Если все проверки пройдены успешно, ответ должен содержать ```<success>true</success>```
  - Если случилась ошибка валидации, ответ должен содержать ```<success>false</success><errors>массив ошибок</errors>```

## Стек
- Spring Boot  
- Spring WS  
- Spring Data JPA   
- PostgreSQL

## XSD-схема
XSD-схема расположена по пути: ```src/main/resources/wsdl/users.xsd```  
Для генерации классов, соответствующих данной схеме, перед запуском spring-приложения необходимо выполнить ```mvn clean install```   
Сгенерированные классы будут расположены по пути: ```src/main/java/generated-sources/ru/codemark/userssoapservice```  

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
           targetNamespace="http://codemark.ru/userssoapservice"
           xmlns:tns="http://codemark.ru/userssoapservice"
           elementFormDefault="qualified">

    <xs:element name="PostUserRequest">
        <xs:complexType>
            <xs:sequence>
                <xs:element name="UserDetails" type="tns:UserDetails"/>
                <xs:element name="role" type="xs:string" maxOccurs="unbounded"/>
            </xs:sequence>
        </xs:complexType>
    </xs:element>

    <xs:element name="PostUserResponse">
        <xs:complexType>
            <xs:sequence>
                <xs:element name="success" type="xs:string"/>
                <xs:element name="error" type="xs:string" minOccurs="0" maxOccurs="unbounded"/>
            </xs:sequence>
        </xs:complexType>
    </xs:element>

    <xs:element name="UpdateUserRequest">
        <xs:complexType>
            <xs:sequence>
                <xs:element name="currentLogin" type="xs:string"/>
                <xs:element name="UpdatedUserDetails" type="tns:UserDetails"/>
                <xs:element name="role" type="xs:string" maxOccurs="unbounded"/>
            </xs:sequence>
        </xs:complexType>
    </xs:element>

    <xs:element name="UpdateUserResponse">
        <xs:complexType>
            <xs:sequence>
                <xs:element name="success" type="xs:string"/>
                <xs:element name="error" type="xs:string" minOccurs="0" maxOccurs="unbounded"/>
            </xs:sequence>
        </xs:complexType>
    </xs:element>

    <xs:element name="DeleteUserRequest">
        <xs:complexType>
            <xs:sequence>
                <xs:element name="login" type="xs:string"/>
            </xs:sequence>
        </xs:complexType>
    </xs:element>

    <xs:element name="DeleteUserResponse">
        <xs:complexType>
            <xs:sequence>
                <xs:element name="success" type="xs:string"/>
                <xs:element name="error" type="xs:string" minOccurs="0" maxOccurs="unbounded"/>
            </xs:sequence>
        </xs:complexType>
    </xs:element>

    <xs:element name="GetUserRequest">
        <xs:complexType>
            <xs:sequence>
                <xs:element name="login" type="xs:string"/>
            </xs:sequence>
        </xs:complexType>
    </xs:element>

    <xs:element name="GetUserResponse">
        <xs:complexType>
            <xs:sequence>
                <xs:element name="success" type="xs:string"/>
                <xs:element name="error" type="xs:string" minOccurs="0" maxOccurs="unbounded"/>
                <xs:element name="UserDetails" type="tns:UserDetails"/>
                <xs:element name="role" type="xs:string" maxOccurs="unbounded"/>
            </xs:sequence>
        </xs:complexType>
    </xs:element>

    <xs:element name="GetUsersRequest">
        <xs:complexType/>
    </xs:element>

    <xs:element name="GetUsersResponse">
        <xs:complexType>
            <xs:sequence>
                <xs:element name="UsersDetails" type="tns:UserDetails" maxOccurs="unbounded"/>
            </xs:sequence>
        </xs:complexType>
    </xs:element>

    <xs:complexType name="UserDetails">
        <xs:sequence>
            <xs:element name="login" type="xs:string"/>
            <xs:element name="password" type="xs:string"/>
            <xs:element name="name" type="xs:string"/>
        </xs:sequence>
    </xs:complexType>

</xs:schema>
```
