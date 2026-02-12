HealthTracker – Service & URL Health Monitoring Platform
HealthTracker is a Spring Boot–based backend system that continuously monitors the health of services and URLs, performs scheduled checks with retry logic, and automatically creates incidents when repeated failures occur.
The project also includes a lightweight React based UI for viewing services, URLs, and incidents. The UI was developed with assistance from AI Copilot to accelerate development.
________________________________________
Features
•	Monitor service and URL availability
•	Scheduled health checks
•	Retry mechanism for transient failures
•	Automatic incident creation (Timeout, Auth Error, General Failure)
•	Incident lifecycle management (Open / Resolved)
•	RESTful APIs for services, URLs, and incidents
•	MongoDB persistence
•	React-based UI dashboard
________________________________________ Tech Stack
Backend
•	Java
•	Spring Boot
•	MongoDB
Frontend
•	React
Tools
•	Maven
•	Git, Postman
•	AI Copilot (UI assistance)
________________________________________
High-Level Architecture
React UI → Spring Boot Controllers → Service Layer → Repository → MongoDB
Scheduler → HealthCheckService → External URLs
________________________________________
API Documentation
Swagger UI:
/swagger-ui/index.html
________________________________________
Example Flow
1.	Create Service
2.	Add URL under Service
3.	Scheduler periodically checks URL
4.	On repeated failure → Incident created
5.	On recovery → Incident resolved
________________________________________ Future Improvements
•	Authentication & authorization
•	Parallel / async health checks
•	Docker support
•	Metrics & monitoring dashboard

