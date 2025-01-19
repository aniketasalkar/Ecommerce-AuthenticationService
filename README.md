# Ecommerce-UserAuthenticationService API Documentation

## Overview
UserAuthentication Service for Ecommerce project.

## Base URL
`/api/auth`

---

## Endpoints

### User Authentication Endpoints

1. **Sign Up User**
   - **URL**: `/signUp`
   - **Method**: `POST`
   - **Request Body**: `SignupRequestDto`
   - **Response**: `SignupResponseDto`
   - **Description**: Registers a new user.

2. **Login User**
   - **URL**: `/login`
   - **Method**: `POST`
   - **Request Body**: `LoginRequestDto`
   - **Response**: `LoginResponseDto`
   - **Description**: Logs in an existing user and returns access and refresh tokens.

3. **Logout User**
   - **URL**: `/{email}/logout`
   - **Method**: `DELETE`
   - **Request Headers**: `Set-Cookie`
   - **Path Variable**: `email`
   - **Response**: `LogOutResponseDto`
   - **Description**: Logs out the user and clears authentication cookies.

4. **Validate and Refresh Token**
   - **URL**: `/{email}/validateAndRefreshToken`
   - **Method**: `POST`
   - **Request Body**: `ValidateAndRefreshTokenRequestDto`
   - **Path Variable**: `email`
   - **Response**: `ValidateAndRefreshTokenResponseDto`
   - **Description**: Validates and refreshes the user's token.

5. **Validate Token**
   - **URL**: `/{email}/validateToken`
   - **Method**: `POST`
   - **Request Body**: `ValidateAndRefreshTokenRequestDto`
   - **Path Variable**: `email`
   - **Response**: `Boolean`
   - **Description**: Validates the user's token.

---

### Service Authentication Endpoints

1. **Register Service**
   - **URL**: `/service/register`
   - **Method**: `POST`
   - **Request Body**: `ServiceRegistryRequestDto`
   - **Response**: `ServiceRegistryResponseDto`
   - **Description**: Registers a new service.

2. **Fetch Service Registry Token**
   - **URL**: `/service/fetch_token/{service_name}`
   - **Method**: `GET`
   - **Path Variable**: `service_name`
   - **Response**: `ServiceRegistryResponseDto`
   - **Description**: Fetches the service registry token.

3. **Validate Service Registry Token**
   - **URL**: `/service/validate_token/`
   - **Method**: `POST`
   - **Request Body**: `ValidateServiceTokenRequestDto`
   - **Response**: `Boolean`
   - **Description**: Validates the service registry token.

---

## Error Handling

The `ControllerAdvisor` handles various exceptions and provides appropriate HTTP responses:
- **UserNotFoundException**: `404 Not Found`
- **UserAlreadyExistsException**: `409 Conflict`
- **BadRequestException**: `400 Bad Request`
- **InvalidTokenException**: `401 Unauthorized`
- **UpdatePasswordException**: `400 Bad Request`
- **PasswordDoesNotMatchException**: `400 Bad Request`
- **NoActiveSessionFoundException**: `404 Not Found`
- **InvalidDataException**: `400 Bad Request`
- **ServiceAlreadyExistsException**: `409 Conflict`
- **DoesNotExistsException**: `404 Not Found`
