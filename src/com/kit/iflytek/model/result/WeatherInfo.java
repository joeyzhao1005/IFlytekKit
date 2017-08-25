package com.kit.iflytek.model.result;

/**
 * Created by Zhao on 16/7/19.
 */
public class WeatherInfo {
    /*
    {
    "data": {
        "result": [
            {
                "airData": 77,
                "airQuality": "良",
                "city": "上海",
                "date": "2017-08-25",
                "dateLong": 1503590400,
                "exp": {
                    "ct": {
                        "expName": "穿衣指数",
                        "level": "炎热",
                        "prompt": "天气炎热，建议着短衫、短裙、短裤、薄型T恤衫等清凉夏季服装。"
                    }
                },
                "humidity": "75%",
                "lastUpdateTime": "2017-08-25 17:04:30",
                "pm25": "34",
                "temp": 30,
                "tempRange": "26℃~35℃",
                "weather": "雷阵雨",
                "weatherType": 4,
                "wind": "西北风微风",
                "windLevel": 0
            },
            {
                "city": "上海",
                "date": "2017-08-26",
                "dateLong": 1503676800,
                "lastUpdateTime": "2017-08-25 17:04:30",
                "tempRange": "26℃~31℃",
                "weather": "雷阵雨转阴",
                "weatherType": 4,
                "wind": "东北风微风",
                "windLevel": 0
            },
            {
                "city": "上海",
                "date": "2017-08-27",
                "dateLong": 1503763200,
                "lastUpdateTime": "2017-08-25 17:04:30",
                "tempRange": "27℃~33℃",
                "weather": "多云",
                "weatherType": 1,
                "wind": "东南风微风",
                "windLevel": 0
            },
            {
                "city": "上海",
                "date": "2017-08-28",
                "dateLong": 1503849600,
                "lastUpdateTime": "2017-08-25 17:04:30",
                "tempRange": "27℃~33℃",
                "weather": "晴转多云",
                "weatherType": 0,
                "wind": "东南风微风",
                "windLevel": 0
            },
            {
                "city": "上海",
                "date": "2017-08-29",
                "dateLong": 1503936000,
                "lastUpdateTime": "2017-08-25 17:04:30",
                "tempRange": "25℃~32℃",
                "weather": "小雨转大雨",
                "weatherType": 7,
                "wind": "东北风4-5级",
                "windLevel": 2
            },
            {
                "city": "上海",
                "date": "2017-08-30",
                "dateLong": 1504022400,
                "lastUpdateTime": "2017-08-25 17:04:30",
                "tempRange": "25℃~29℃",
                "weather": "小雨",
                "weatherType": 7,
                "wind": "东北风3-4级",
                "windLevel": 1
            },
            {
                "city": "上海",
                "date": "2017-08-31",
                "dateLong": 1504108800,
                "lastUpdateTime": "2017-08-25 17:04:30",
                "tempRange": "25℃~30℃",
                "weather": "中雨转小雨",
                "weatherType": 8,
                "wind": "东风微风",
                "windLevel": 0
            }
        ]
    },
    "rc": 0,
    "semantic": [
        {
            "intent": "QUERY",
            "slots": [
                {
                    "name": "location.city",
                    "value": "上海市",
                    "normValue": "上海市"
                },
                {
                    "name": "location.cityAddr",
                    "value": "上海",
                    "normValue": "上海"
                },
                {
                    "name": "location.type",
                    "value": "LOC_BASIC",
                    "normValue": "LOC_BASIC"
                },
                {
                    "name": "queryType",
                    "value": "内容"
                },
                {
                    "name": "subfocus",
                    "value": "天气状态"
                }
            ]
        }
    ],
    "service": "weather",
    "text": "上海的天气怎么样",
    "uuid": "atn000671ed@chbe070cfb47786f2a01",
    "used_state": {
        "state_key": "fg::weather::default::default",
        "state": "default"
    },
    "answer": {
        "text": "\"上海\"今天\"雷阵雨\"，\"26℃~35℃\"，\"西北风微风\"，雨天出行记得准备雨伞"
    },
    "dialog_stat": "DataValid",
    "sid": "atn000671ed@chbe070cfb47786f2a01"
}

     */


    public int getAirData() {
        return airData;
    }

    public void setAirData(int airData) {
        this.airData = airData;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public long getDateLong() {
        return dateLong;
    }

    public void setDateLong(long dateLong) {
        this.dateLong = dateLong;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAirQuality() {
        return airQuality;
    }

    public void setAirQuality(String airQuality) {
        this.airQuality = airQuality;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public Exp getExp() {
        return exp;
    }

    public void setExp(Exp exp) {
        this.exp = exp;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public String getTempRange() {
        return tempRange;
    }

    public void setTempRange(String tempRange) {
        this.tempRange = tempRange;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public int getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(int weatherType) {
        this.weatherType = weatherType;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public int getWindLevel() {
        return windLevel;
    }

    public void setWindLevel(int windLevel) {
        this.windLevel = windLevel;
    }

    private int airData;
    private String city;

    private long dateLong;
    private String date;

    private String airQuality;

    private String lastUpdateTime;

    private String humidity;
    private Exp exp;


    private String pm25;

    private int temp;
    private String tempRange;
    private String weather;
    private int weatherType;
    private String wind;
    private int windLevel;


    public class Exp {

        public CT getCt() {
            return ct;
        }

        public void setCt(CT ct) {
            this.ct = ct;
        }

        private CT ct;

        public class CT {

            public String getExpName() {
                return expName;
            }

            public void setExpName(String expName) {
                this.expName = expName;
            }

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public String getPrompt() {
                return prompt;
            }

            public void setPrompt(String prompt) {
                this.prompt = prompt;
            }

            private String expName;

            private String level;

            private String prompt;

        }
    }


}
