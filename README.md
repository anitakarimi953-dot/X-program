# X-Program

## Java Client-Server Social Network Application

X-Program is a simple social media application inspired by Twitter/X, developed using Java.

The project uses a **Client-Server architecture** where clients communicate with the server using socket programming.

Users can register, login, create tweets, follow other users, view profiles, and manage their sessions.

---

# Features

## User Management

- User registration
- User login
- Password hashing
- User authentication
- Session management

## Tweet System

- Create tweets
- View tweet feed
- Display tweet author
- Display tweet creation time

## Follow System

- Follow users
- Unfollow users
- View followers
- View following users

## Profile System

- View user profiles
- Display username
- Display followers count
- Display following count

## Session System

- Create session after login
- Validate sessions
- Logout functionality

---

# Project Architecture

The project follows a Client-Server architecture.

```
                 Client
                    |
                    |
              Socket Connection
                    |
                    |
                 Server
                    |
        ---------------------------
        |            |            |
        |            |            |
 UserManager   TweetManager   SessionManager
```

The client is responsible for user interaction and GUI.

The server handles all business logic and data processing.

---

# Technologies Used

- Java
- Java Swing
- Socket Programming
- Gson Library
- JSON Communication
- Object-Oriented Programming (OOP)

---

# Project Structure

```
X-Program

├── client
│
│   ├── Client.java
│   ├── DashboardFrame.java
│   └── GUI Classes
│
├── server
│
│   ├── Server.java
│   ├── UserManager.java
│   ├── TweetManager.java
│   ├── SessionManager.java
│   ├── PasswordHasher.java
│   └── LocalDateTimeAdapter.java
│
└── common
    │
    ├── User.java
    ├── Tweet.java
    ├── Message.java
    └── Profile.java
```

---

# Client-Server Communication

Communication between client and server is done using Socket Programming.

The client sends requests to the server as JSON messages.

Example:

```json
{
  "type": "LOGIN",
  "content": "username|password"
}
```

The server processes the request and returns a response.

Example:

```
LOGIN_SUCCESS|sessionId
```

---

# Message Handling

All requests are handled using the `Message` class.

The message contains:

- Request type
- Request content

Examples:

```
REGISTER
LOGIN
LOGOUT
CREATE_TWEET
GET_TWEETS
FOLLOW
UNFOLLOW
GET_PROFILE
```

The server uses these commands to execute the correct operation.

---

# Authentication System

Passwords are not stored directly.

Before saving a password, it is hashed.

Flow:

```
User Password

       |
       v

Password Hasher

       |
       v

Stored Hash
```

During login:

```
Entered Password

       |
       v

Hash Password

       |
       v

Compare With Stored Password
```

This improves security by avoiding storing plain passwords.

---

# Session Management

After successful login, the server creates a unique session ID.

The session is managed by `SessionManager`.

Protected operations require a valid session:

- Creating tweets
- Following users
- Viewing profiles
- Getting followers
- Getting following list

Example:

```
LOGIN

   |
   v

Create Session

   |
   v

Return Session ID
```

---

# Tweet System

Tweets are managed by `TweetManager`.

Each tweet contains:

- Tweet ID
- Username
- Content
- Creation time


Tweet creation flow:

```
User

 |

Dashboard

 |

Client

 |

Server

 |

TweetManager

 |

Create Tweet
```

---

# Tweet Feed

The feed displays all created tweets.

The client sends:

```
GET_TWEETS
```

request.

The server returns tweets as JSON.

The client converts JSON back to Tweet objects and displays them in the GUI.

Example:

```
@username

Hello World!

2026-08-01 18:30
-----------------------
```

---

# Follow System

Following another user:

```
Client

 |

Server

 |

Check Session

 |

Find Target User

 |

Update Following List

 |

Return Result
```

The system prevents:

- Following yourself
- Following non-existing users

---

# Profile System

Users can view profiles.

Profile information includes:

- Username
- Followers count
- Following count


Profile request flow:

```
Client

 |

Server

 |

Find User

 |

Calculate Followers/Following

 |

Return Profile
```

---

# Date and Time Handling

Tweets store creation time using:

```
LocalDateTime
```

Because Gson cannot directly serialize LocalDateTime, a custom adapter:

```
LocalDateTimeAdapter
```

is used.

This allows correct conversion between Java objects and JSON.

---

# Error Handling

The application handles different errors.

Examples:

```
LOGIN_FAILED

REGISTER_FAILED

SESSION_INVALID

CONNECTION_ERROR

FOLLOW_FAILED

UNFOLLOW_FAILED

TWEET_CREATE_FAILED
```

The client displays appropriate messages to the user.

---

# How to Run

## 1. Start Server

Run:

```
server.Server
```

The server starts listening on the configured port.

Example:

```
Server started on port 5000
```

---

## 2. Start Client

Run:

```
client.Main
```

The graphical interface will open.

---

# Demo Scenario

Recommended demonstration:

1. Register a user
2. Login
3. Create a tweet
4. Refresh feed
5. Register another user
6. Login with second user
7. Follow first user
8. Check followers/following
9. View profile
10. Logout

---

# Main Concepts Implemented

This project demonstrates:

- Client-Server architecture
- Socket programming
- GUI programming
- JSON communication
- Object-Oriented Programming
- Authentication
- Session management
- User relationship management

---

# Conclusion

X-Program demonstrates a complete Java network application where multiple clients can communicate with a centralized server.

The project focuses on practical implementation of networking concepts, user management, authentication, and social media features.