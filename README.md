# Wolt task: openinghours


# Prerequisites
- Maven 3.x
- Java 11

# Build and run
- ./buildAndRun.sh
  - mvn clean install
  - docker build --tag=wildfly-openinghours .
  - docker run -it -p 8080:8080 -p 9990:9990 wildfly-openinghours

# Rest URL
- http://localhost:8080/openinghours/api/hours

# Example HTTP POST Json payload
```json
{
	"monday": [
		{
			"type": "close",
			"value": 3600
		}
	],
	"tuesday": [
		{
			"type": "open",
			"value": 36000
		},
		{
			"type": "close",
			"value": 64800
		}
	],
	"wednesday": [],
	"thursday": [
		{
			"type": "open",
			"value": 36000
		},
		{
			"type": "close",
			"value": 64800
		}
	],
	"friday": [
		{
			"type": "open",
			"value": 36000
		}
	],
	"saturday": [
		{
			"type": "close",
			"value": 3600
		},
		{
			"type": "open",
			"value": 36000
		}
	],
	"sunday": [
		{
			"type": "close",
			"value": 3600
		},
		{
			"type": "open",
			"value": 43200
		}
	]
}
```
