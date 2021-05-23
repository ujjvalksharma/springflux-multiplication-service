# springflux-multiplication-service
This is a spring flux multiplication service using multithreading, retry, backpressure, schedular threads, resilient.

#API
GET: http://localhost:8081/api/square/10
GET: http://localhost:8081/api/multitable/10
GET: http://localhost:8081/api/multitable/10/stream
POST: http://localhost:8081/api/multiply
Body: {
	"first":10,
	"second":20
}
