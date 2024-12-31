# Interview-quiz Workspace Setup

Prerequisites:
- Java 17 or later
- Maven 3 (https://maven.apache.org/)
- Redis server (https://redis.io/) (Optional, only needed if using Spring Cache with Redis)

Bot Configuration (Replace with your own values):
- Client ID: Replace ADD_YOUR_CLIENT_ID with your bot's client ID.

Spring Configuration:
- Cache Type: Redis (Can be changed to other caching providers)
- Redis Host: localhost (Update if your Redis server is running on a different host)
- Redis Port: 6379 (Update if your Redis server uses a different port)

Steps:
1. Clone the repository
    git clone https://github.com/thummanunpy/interview-quiz.git

2. Install Dependencies:
   cd interview-quiz
   mvn clean install

4. (Optional) Configure Redis:
   If you are not using Spring Cache with Redis, you can skip this step.
   Configure your local Redis server according to its documentation.

5. Replace Bot Configuration Placeholders:
    Replace ADD_YOUR_CLIENT_ID with your bot's client ID in the following files:
    - src/main/resources/application.properties

6. Run the Application:
   -  mvn spring-boot:run



# Interview Quiz

This assignment is an application to retrieve information about financial institution holiday.

---

The service provides 3 APIs to get holiday data

- The first one is an API to get all holiday in year from parameter 'year'.
```
path: /holiday
method: GET
params: year (optional)
```

Example response:
```json
[
   {
      "HolidayWeekDay": "Monday",
      "HolidayWeekDayThai": "วันจันทร์",
      "Date": "2023-01-02",
      "DateThai": "02/01/2566",
      "HolidayDescription": "Substitution for New Year's Eve and New Year's Day (Saturday 31st  December 2022 and Saturday 1st January 2023)",
      "HolidayDescriptionThai": "ชดเชยวันสิ้นปีและวันขึ้นปีใหม่ (วันเสาร์ที่ 31 ธันวาคม 2565 และวันอาทิตย์ที่ 1 มกราคม 2566)"
   },
   {
      "HolidayWeekDay": "Monday",
      "HolidayWeekDayThai": "วันจันทร์",
      "Date": "2023-03-06",
      "DateThai": "06/03/2566",
      "HolidayDescription": "Makha Bucha Day",
      "HolidayDescriptionThai": "วันมาฆบูชา"
   },
   ...
]
```

- The second API is to find the nearest holiday in the future from now.
```
path: /holiday/nearest
method: GET
params: none
```

Example response:
```json
{
   "HolidayWeekDay": "Monday",
   "HolidayWeekDayThai": "วันจันทร์",
   "Date": "2023-01-02",
   "DateThai": "02/01/2566",
   "HolidayDescription": "Substitution for New Year's Eve and New Year's Day (Saturday 31st  December 2022 and Saturday 1st January 2023)",
   "HolidayDescriptionThai": "ชดเชยวันสิ้นปีและวันขึ้นปีใหม่ (วันเสาร์ที่ 31 ธันวาคม 2565 และวันอาทิตย์ที่ 1 มกราคม 2566)"
}
```

- The last API will find all holiday in range of date.
```
path: /holiday/range
method: GET
params: startDate(format is yyyy-MM-dd), endDate(format is yyyy-MM-dd)
```

Example response:
```json
[
   {
      "HolidayWeekDay": "Monday",
      "HolidayWeekDayThai": "วันจันทร์",
      "Date": "2023-01-02",
      "DateThai": "02/01/2566",
      "HolidayDescription": "Substitution for New Year's Eve and New Year's Day (Saturday 31st  December 2022 and Saturday 1st January 2023)",
      "HolidayDescriptionThai": "ชดเชยวันสิ้นปีและวันขึ้นปีใหม่ (วันเสาร์ที่ 31 ธันวาคม 2565 และวันอาทิตย์ที่ 1 มกราคม 2566)"
   },
   {
      "HolidayWeekDay": "Monday",
      "HolidayWeekDayThai": "วันจันทร์",
      "Date": "2023-03-06",
      "DateThai": "06/03/2566",
      "HolidayDescription": "Makha Bucha Day",
      "HolidayDescriptionThai": "วันมาฆบูชา"
   },
   ...
]
```

### Tasks

You have to do all the tasks to complete the assignment.

1. Create a web client to retrieve data from the public API.
2. Using the HolidayService to generate data from public API.
3. Verify that the results of all APIs are as expected.

Note:
- Fix some issues that may occur when implementing the API.
- All unit tests should be passed and coverage is 100%.
- You can you everything to improve performance such as cache.
- Push the project to your personal git and set it to public (we will see how you work by the commits).

---

### Public API Documents

https://apiportal.bot.or.th/bot/public/node/104
