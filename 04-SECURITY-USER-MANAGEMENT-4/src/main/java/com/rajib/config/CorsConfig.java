package com.rajib.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

	@Bean
	public WebMvcConfigurer webMvcConfigurer() {
		return new WebMvcConfigurer() {
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
				.allowedMethods("GET", "POST", "PUT", "DELETE")
				.allowedOrigins("*");
			}
		};
	}
}

/* Perfect, Rajib! If you're using **React as your frontend** and **Spring Boot as your backend**, this `CorsConfig` is already almost good to go — but let me tailor it **specifically for React development**.

---

### ✅ React typically runs on:
```
http://localhost:3000
```

So, instead of allowing requests from `"*"` (any origin), it's better to allow only from your React app’s origin.

---

### 🔧 Modify Your `CorsConfig` Like This:
```java
@Configuration
public class CorsConfig {

	@Bean
	public WebMvcConfigurer webMvcConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
					.allowedOrigins("http://localhost:3000")  // Only allow React app
					.allowedMethods("GET", "POST", "PUT", "DELETE")
					.allowedHeaders("*")
					.allowCredentials(true);  // If you're using cookies or authentication
			}
		};
	}
}
```

---

### ✅ Frontend React Code: Use `fetch` or `axios` like this:

If using **Axios**, configure it like this:
```javascript
import axios from 'axios';

axios.defaults.baseURL = 'http://localhost:8080'; // your Spring Boot port
axios.defaults.withCredentials = true; // if using sessions or cookies
```

Example call:
```javascript
axios.get('/api/users')
  .then(res => console.log(res.data))
  .catch(err => console.error(err));
```

---

### ⚠️ Why `allowCredentials(true)`?

- If you’re using **session-based login**, or you’re setting **cookies**, then you must set:
  - In Spring Boot: `.allowCredentials(true)`
  - In React (Axios or fetch): `withCredentials: true`

If you're just using token-based auth (like JWT in headers), you may not need `allowCredentials`.

---

### ✅ Bonus Tip:
Make sure your **Spring Boot backend** is running on a different port (like `8080`) and **React on `3000`**, otherwise CORS isn't even triggered.

---

Let me know if you're using login/auth or want to deploy this React + Spring Boot app — I can help you with CORS for production too!
 */