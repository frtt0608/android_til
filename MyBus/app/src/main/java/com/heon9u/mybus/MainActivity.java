package com.heon9u.mybus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    TextView busNumer;
    String getData;
    int busNumber = 0;
    int busNum = 503;
    String strSrch = busNumber + "";
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.data);
        busNumer = (TextView) findViewById(R.id.bus);

        //     String serviceUrl = "http://ws.bus.go.kr/api/rest/busRouteInfo/getBusRouteList";

        String serviceUrl = "http://ws.bus.go.kr/api/rest/busRouteInfo/getBusRouteList";

        String serviceKey = "lI0Ngu5M%2BJewHO%2BGcadPtiLVGTc6U4lgwJW8sgKiDLQVZ31N%2BwpXpAiPGoFaJJE0VsqzxAZNruDG8BC3kxkFhQ%3D%3D";

        //가져올 정보를 strUrl에 저장함
        String strUrl = serviceUrl + "?ServiceKey=" + serviceKey + "&strSrch=" + strSrch;

        DownloadWebContent dwc1 = new DownloadWebContent();
        dwc1.execute(strUrl);
    }

    public class DownloadWebContent extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return (String) downloadByUrl((String) urls[0]);
            } catch (IOException e) {
                System.out.println("첫 번째 content");
                System.out.println("다운로드 실패");
                return "다운로드 실패";
            }
        }

        protected void onPostExecute(String result) {
            //xml 문서를 파싱하는 방법으로 본 예제에서는  Pull Parer 를 사용한다.
            String headerCd = "";
            String busRouteId = "";

            boolean bus_headerCd = false;
            boolean bus_busRouteId = false;

            // textView.append("===== 노선ID =====\n");
            try {

                //XmlPullParser 를 사용하기 위해서 XmlPullParserFactory 객체를 생성함
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xmlpp = factory.newPullParser();

                //parser 에 url를 입력함
                xmlpp.setInput(new StringReader(result));


                //parser 이벤트를 저장할 변수 지정
                int eventType = xmlpp.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {   //문서가 마지막이 아니면
                    if (eventType == XmlPullParser.START_DOCUMENT) {
                        ;
                    } else if (eventType == XmlPullParser.START_TAG) {
                        String tag_name = xmlpp.getName();
                        if (tag_name.equals("headerCd"))
                            bus_headerCd = true;
                        if (tag_name.equals("busRouteId"))
                            bus_busRouteId = true;

                    } else if (eventType == XmlPullParser.TEXT) {
                        if (bus_headerCd) {
                            headerCd = xmlpp.getText();
                            bus_headerCd = false;
                        }

                        if (headerCd.equals("0")) {
                            if (bus_busRouteId) {
                                busRouteId = xmlpp.getText();
                                bus_busRouteId = false;
                            }

                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        ;
                    }
                    eventType = xmlpp.next();
                }
            } catch (Exception e) {
                textView.setText(e.getMessage());
            }

            String serviceUrl = "http://ws.bus.go.kr/api/rest/busRouteInfo/getStaionByRoute";

            String serviceKey = "lI0Ngu5M%2BJewHO%2BGcadPtiLVGTc6U4lgwJW8sgKiDLQVZ31N%2BwpXpAiPGoFaJJE0VsqzxAZNruDG8BC3kxkFhQ%3D%3D";

            String strUrl = serviceUrl + "?ServiceKey=" + serviceKey + "&busRouteId=" + busRouteId;

            DownloadWebContent2 dwc2 = new DownloadWebContent2();
            dwc2.execute(strUrl);
        }

        public String downloadByUrl(String myurl) throws IOException {
            //Http 통신: HttpURLConnection 클래스를 활용해 데이터를 얻는다.

            HttpURLConnection conn = null;
            try {
                //요청 URL, 전달받은 url string 으로 URL 객체를 만듦
                URL url = new URL(myurl);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                System.out.println(conn);

                InputStream is = conn.getInputStream();
                System.out.println("is");

                BufferedInputStream buffer = new BufferedInputStream(conn.getInputStream());
                BufferedReader buffer_reader = new BufferedReader(new InputStreamReader(buffer, "utf-8"));

                String line = null;
                getData = "";
                while ((line = buffer_reader.readLine()) != null) {
                    getData += line;

                }

                return getData;
            } finally {
                //접속 해제
                conn.disconnect();
            }
        }

    }

    public class DownloadWebContent2 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return (String) downloadByUrl((String) urls[0]);
            } catch (IOException e) {
                System.out.println("두번째 Content");
                System.out.println("다운로드 실패");
                return "다운로드 실패";
            }
        }

        protected void onPostExecute(String result) {
            String headerCd = "";
            String gpsX = "";
            String gpsY = "";
            String stationNm = "";
            String direction = "";
            String sectSpd = "";

            boolean bus_headerCd = false;
            boolean bus_gpsX = false;
            boolean bus_gpsY = false;
            boolean bus_stationNm = false;
            boolean bus_sectSpd = false;
            boolean bus_direction = false;
            ///// (2) Bus Positions
            textView.append("-버스 위치 검색 결과-\n");
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xmlpp = factory.newPullParser();

                xmlpp.setInput(new StringReader(result));
                int eventType = xmlpp.getEventType();

                count = 0;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_DOCUMENT) {
                        ;
                    } else if (eventType == XmlPullParser.START_TAG) {
                        String tag_name = xmlpp.getName();

                        switch (tag_name) {

                            case "headerCd":
                                bus_headerCd = true;
                                break;
                            case "gpsX":
                                bus_gpsX = true;
                                break;
                            case "gpsY":
                                bus_gpsY = true;
                                break;
                            case "sectSpd":
                                bus_sectSpd = true;
                                break;
                            case "stationNm":
                                bus_stationNm = true;
                                break;
                            case "direction":
                                bus_direction = true;
                                break;
                        }

                    } else if (eventType == XmlPullParser.TEXT) {
                        if (bus_headerCd) {
                            headerCd = xmlpp.getText();
                            // textView.append("headerCd: " + headerCd + "\n");
                            bus_headerCd = false;
                        }

                        if (headerCd.equals("0")) {
                            if (bus_gpsX) {
                                count++;
                                textView.append("-------------------------------------------\n");

                                gpsX = xmlpp.getText();
                                textView.append("(" + count + ") gpsX: " + gpsX + "\n");
                                bus_gpsX = false;
                            }
                            if (bus_gpsY) {
                                gpsY = xmlpp.getText();
                                textView.append("(" + count + ") gpsY: " + gpsY + "\n");
                                bus_gpsY = false;
                            }

                            if (bus_stationNm) {
                                stationNm = xmlpp.getText();
                                textView.append("(" + count + ") 정류장이름: " + stationNm + "\n");
                                bus_stationNm = false;
                            }

                            if (bus_direction) {
                                direction = xmlpp.getText();
                                textView.append("(" + count + ") 진행방향: " + direction + "\n");
                                bus_direction = false;
                            }

                            if (bus_sectSpd) {
                                sectSpd = xmlpp.getText();
                                textView.append("(" + count + ") 구간속도: " + sectSpd + "\n");
                                bus_sectSpd = false;
                            }

                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        ;
                    }
                    eventType = xmlpp.next();
                }
            } catch (Exception e) {
                textView.setText(e.getMessage());
            }

        }


        public String downloadByUrl(String myurl) throws IOException {
            //Java와 Http 통신: HttpURLConnection 클래스를 활용해 데이터를 얻는다.

            HttpURLConnection conn = null;
            BufferedReader buffer_reader;
            try {
                //요청 URL, 전달받은 url string 으로 URL 객체를 만듦
                URL url = new URL(myurl);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                //결과를 InputStream으로 받아서 INputStreamReader, BufferedReader로 캐스팅한다.
                BufferedInputStream buffer = new BufferedInputStream(conn.getInputStream());
                buffer_reader = new BufferedReader(new InputStreamReader(buffer, "utf-8"));

                String line = null;
                getData = "";
                while ((line = buffer_reader.readLine()) != null) {
                    getData += line;

                }

                return getData;
            } finally {
                //접속 해제
                conn.disconnect();
            }
        }
    }

    public void plusBusNumber(View v) {

        busNum += 1;

        String serviceUrl = "http://ws.bus.go.kr/api/rest/busRouteInfo/getBusRouteList";
        String serviceKey = "lI0Ngu5M%2BJewHO%2BGcadPtiLVGTc6U4lgwJW8sgKiDLQVZ31N%2BwpXpAiPGoFaJJE0VsqzxAZNruDG8BC3kxkFhQ%3D%3D";
        strSrch = busNum + "";

        //가져올 정보를 strUrl에 저장함
        String strUrl = serviceUrl + "?ServiceKey=" + serviceKey + "&strSrch=" + strSrch;
//
        DownloadWebContent dwc1 = new DownloadWebContent();
        dwc1.execute(strUrl);
        textView.setText("");
        busNumer.setText("");
        busNumer.append("버스번호:");
        busNumer.append(strSrch + "\n");
    }

    public void minusBusNumber(View v) {

        busNum -= 1;

        String serviceUrl = "http://ws.bus.go.kr/api/rest/busRouteInfo/getBusRouteList";
        String serviceKey = "lI0Ngu5M%2BJewHO%2BGcadPtiLVGTc6U4lgwJW8sgKiDLQVZ31N%2BwpXpAiPGoFaJJE0VsqzxAZNruDG8BC3kxkFhQ%3D%3D";
        strSrch = busNum + "";

        //가져올 정보를 strUrl에 저장함
        String strUrl = serviceUrl + "?ServiceKey=" + serviceKey + "&strSrch=" + strSrch;
//
        DownloadWebContent dwc1 = new DownloadWebContent();
        dwc1.execute(strUrl);
        textView.setText("");
        busNumer.setText("");
        busNumer.append("버스번호:");
        busNumer.append(strSrch + "\n");
    }

    public void resetCurrentBus(View v) {

        //  busNum+=1;
        String serviceUrl = "http://ws.bus.go.kr/api/rest/busRouteInfo/getBusRouteList";
        String serviceKey = "lI0Ngu5M%2BJewHO%2BGcadPtiLVGTc6U4lgwJW8sgKiDLQVZ31N%2BwpXpAiPGoFaJJE0VsqzxAZNruDG8BC3kxkFhQ%3D%3D";
        strSrch = busNum + "";

        //가져올 정보를 strUrl에 저장함
        String strUrl = serviceUrl + "?ServiceKey=" + serviceKey + "&strSrch=" + strSrch;

        DownloadWebContent dwc1 = new DownloadWebContent();
        dwc1.execute(strUrl);
        textView.setText("");
        busNumer.setText("");
        busNumer.append("버스번호:");
        busNumer.append(strSrch + "\n");
    }

    public void plusBaek(View v) {
        busNum += 100;
        String serviceUrl = "http://ws.bus.go.kr/api/rest/busRouteInfo/getBusRouteList";
        String serviceKey = "lI0Ngu5M%2BJewHO%2BGcadPtiLVGTc6U4lgwJW8sgKiDLQVZ31N%2BwpXpAiPGoFaJJE0VsqzxAZNruDG8BC3kxkFhQ%3D%3D";
        strSrch = busNum + "";

        //가져올 정보를 strUrl에 저장함
        String strUrl = serviceUrl + "?ServiceKey=" + serviceKey + "&strSrch=" + strSrch;

        DownloadWebContent dwc1 = new DownloadWebContent();
        dwc1.execute(strUrl);
        textView.setText("");
        busNumer.setText("");
        busNumer.append("버스번호:");
        busNumer.append(strSrch + "\n");
    }

    public void minusBaek(View v) {
        busNum -= 100;
        String serviceUrl = "http://ws.bus.go.kr/api/rest/busRouteInfo/getBusRouteList";
        String serviceKey = "lI0Ngu5M%2BJewHO%2BGcadPtiLVGTc6U4lgwJW8sgKiDLQVZ31N%2BwpXpAiPGoFaJJE0VsqzxAZNruDG8BC3kxkFhQ%3D%3D";
        strSrch = busNum + "";

        //가져올 정보를 strUrl에 저장함
        String strUrl = serviceUrl + "?ServiceKey=" + serviceKey + "&strSrch=" + strSrch;

        DownloadWebContent dwc1 = new DownloadWebContent();
        dwc1.execute(strUrl);
        textView.setText("");
        busNumer.setText("");
        busNumer.append("버스번호:");
        busNumer.append(strSrch + "\n");

    }


}


