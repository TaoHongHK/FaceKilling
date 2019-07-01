package com.example.facekilling.util;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.facekilling.javabean.Cof;
import com.example.facekilling.javabean.Expression;
import com.example.facekilling.javabean.Face;
import com.example.facekilling.javabean.Information;
import com.example.facekilling.javabean.MainUser;
import com.example.facekilling.javabean.Picture;
import com.example.facekilling.javabean.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.support.constraint.Constraints.TAG;
import static com.example.facekilling.util.JsonUtil.decodeCofListFromCofString;
import static com.example.facekilling.util.JsonUtil.decodeErrorFronJson;
import static com.example.facekilling.util.JsonUtil.decodeIDFromJson;
import static com.example.facekilling.util.StaticConstant.COF_COMMENT;
import static com.example.facekilling.util.StaticConstant.COF_LIKE;
import static com.example.facekilling.util.StaticConstant.COF_REMOVE_COMMENT;
import static com.example.facekilling.util.StaticConstant.COF_UNLIKE;
import static com.example.facekilling.util.StaticConstant.DELETE_COF;
import static com.example.facekilling.util.StaticConstant.FACE_ATTRIBUTE;
import static com.example.facekilling.util.StaticConstant.FACE_EXPRESSION;
import static com.example.facekilling.util.StaticConstant.FRIEND_ADD;
import static com.example.facekilling.util.StaticConstant.FRIEND_DELETE;
import static com.example.facekilling.util.StaticConstant.GET_ID;
import static com.example.facekilling.util.StaticConstant.REFRESH_MSG;
import static com.example.facekilling.util.StaticConstant.SEND_COF_IMG;
import static com.example.facekilling.util.StaticConstant.SEND_COF_MESSAGE;
import static com.example.facekilling.util.StaticConstant.SEND_MSG;

public class OkHttpUtils {

    private static OkHttpClient client = new OkHttpClient();

    /**
     * @param email
     * @param password
     * @return (> 0)->success (=-1)->pwd wrong (=-2)->email wrong
     */
    public static int logIn(String email, String password) {
        int result = -1;
        final Request request = new Request.Builder()
                .url(StaticConstant.LOGIN + "?email=" + email + "&password=" + password)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String json = response.body().string();
                result = decodeIDFromJson(json);
                int error = decodeErrorFronJson(json);
                if (error == 3) {
                    result = -1;
                } else if (error == 4) {
                    result = -2;
                }
                response.body().close();
            } else {
                throw new IOException("Unexpected code:" + response);
            }
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param nickName
     * @param email
     * @param password
     * @param mobile
     * @param gender
     * @param address
     * @param path
     * @return result (>0)->success (=-1)->gender wrong (=-2)->email wrong
     */
    public static int signUp(String nickName, String email, String password,
                             String mobile, String gender, String address, String path) {
        int result = -1;
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                        "email",
                        email)
                .addFormDataPart(
                        "password",
                        password)
                .addFormDataPart(
                        "nickname",
                        nickName)
                .addFormDataPart(
                        "head",
                        path,
                        RequestBody.create(MediaType.parse("image/jpg"), new File(path)))
                .addFormDataPart(
                        "gender",
                        gender)
                .addFormDataPart(
                        "phone",
                        mobile)
                .addFormDataPart(
                        "address",
                        address);
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(StaticConstant.REGISTER).post(requestBody).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String json = response.body().string();
                result = decodeIDFromJson(json);
                int error = decodeErrorFronJson(json);
                if (error == 1) {
                    result = -1;
                } else if (error == 2) {
                    result = -2;
                }
                response.body().close();
            } else {
                throw new IOException("Unexpected code:" + response);
            }
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * @param id
     */
    public static MainUser getMainUserInfo(int id) {
        MainUser mainUser = null;
        //发起请求
        final Request request = new Request.Builder()
                .url(StaticConstant.GET_ACCOUNT_INFO + "?id=" + id)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                mainUser = JsonUtil.decodeMainUserFronJson(response.body().string(), id);
                response.body().close();
            } else {
                throw new IOException("Unexpected code:" + response);
            }
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mainUser;
    }

    /**
     * @param id
     */
    public static User getUserInfo(int id) {
        User user = null;
        //发起请求
        final Request request = new Request.Builder()
                .url(StaticConstant.GET_ACCOUNT_INFO + "?id=" + id)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                user = JsonUtil.decodeUserFronJson(response.body().string(), id);
                response.body().close();
            } else {
                throw new IOException("Unexpected code:" + response);
            }
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * @param id
     */
    public static List<Integer> getFriendIdList(int id) {
        List<Integer> friendIdList = null;
        final Request request = new Request.Builder()
                .url(StaticConstant.GET_ACCOUNT_INFO + "?id=" + id)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                JSONObject jsonObject = new JSONObject(response.body().string());
                friendIdList = JsonUtil.getFrindsList(jsonObject);
                response.body().close();
            } else {
                throw new IOException("Unexpected code:" + response);
            }
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return friendIdList;
    }


    /**
     * @param email
     * @param password
     */
    public boolean dleteAccount(String email, String password) {
        boolean result = false;
        FormBody.Builder builder = new FormBody.Builder()
                .add("email", email)
                .add("password", password);
        Request request = new Request.Builder().url(StaticConstant.DELETE_ACCOUNT)
                .post(builder.build()).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String json = response.body().string();
                result = (decodeIDFromJson(json) != -1);
                response.body().close();
            }
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param email
     * @param phone
     * @param newPassword
     */
    public boolean forgetPassword(String email, String phone, String newPassword) {
        boolean result = false;
        FormBody.Builder builder = new FormBody.Builder()
                .add("email", email)
                .add("phone", phone)
                .add("newPassword", newPassword);
        Request request = new Request.Builder().url(StaticConstant.FORGET_PWD)
                .post(builder.build()).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String json = response.body().string();
                result = (decodeIDFromJson(json) != -1);
                response.body().close();
            }
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * @param id
     * @param password
     * @param newEmail
     * @param newPassword
     * @param newNickname
     * @param newHead
     * @param newGender
     * @param newPhone
     * @param newAddress
     */
    public static boolean changeAccountInfo(int id, String email, String password, String newEmail,
                                            String newPassword, String newNickname, String newHead, String newGender,
                                            String newPhone, String newAddress) {
        boolean result = false;
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id", Integer.toString(id))
                .addFormDataPart("email", email)
                .addFormDataPart("password", password)
                .addFormDataPart("newEmail", newEmail)
                .addFormDataPart("newPassword", newPassword)
                .addFormDataPart("newNickname", newNickname)
                .addFormDataPart("newGender", newGender)
                .addFormDataPart("newPhone", newPhone)
                .addFormDataPart("newAddress", newAddress);
        if (null != newHead && !"".equals(newHead) && new File(newHead).exists())
            builder.addFormDataPart("newHead", newHead, RequestBody.create(MediaType.parse("image/jpg"), new File(newHead)));
        Request request = new Request.Builder().url(StaticConstant.CHANGE_ACCOUNT).post(builder.build()).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String json = response.body().string();
                result = (decodeIDFromJson(json) != -1);
                response.body().close();
            }
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 根据email获取id
     *
     * @param email
     */
    public static int getIdByEmail(final String email) {
        final int[] id = {-1};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(GET_ID + "?email=" + email)
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (response.code() == 200) {
                        String result = response.body().string();
                        JSONObject jObject1 = new JSONObject(result);
                        id[0] = jObject1.getInt("id");
                        Log.d(TAG, "getIdByEmail: " + result);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (response != null) {
                        response.body().close();
                    }
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return id[0];
    }

    /**
     * 根据id获取email
     *
     * @param id
     */
    public static String getEmailById(final int id) {
        final String[] email = {null};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(GET_ID + "?id=" + id)
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (response.code() == 200) {
                        String result = response.body().string();
                        JSONObject jObject1 = new JSONObject(result);
                        email[0] = jObject1.getString("email");
                        Log.d(TAG, "getIdByEmailY: " + result);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (response != null) {
                        response.body().close();
                    }
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return email[0];
    }


    /**
     * 添加好友
     *
     * @param user
     * @param email
     * @throws IOException
     * @throws JSONException
     */
    public static int postAddFriend(User user, String email) throws IOException {
        int friendId = getIdByEmail(email);
        RequestBody requestBody = new FormBody.Builder()
                .add("id", Integer.toString(user.getUser_id()))
                .add("id_friend", Integer.toString(friendId))
                .build();
        Request request = new Request.Builder()
                .url(FRIEND_ADD)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        String string = response.body().string();
        Log.d("addFriend", "addFriend: " + string);
        int error = -1;
        try {
            error = decodeErrorFronJson(string);
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return error;
    }

    /**
     * 删除好友
     *
     * @param user
     * @param email
     * @throws IOException
     * @throws JSONException
     */
    public static boolean postDeleteFriend(User user, String email) throws IOException, JSONException {
        int friendId = getIdByEmail(email);
        RequestBody requestBody = new FormBody.Builder()
                .add("id", Integer.toString(user.getUser_id()))
                .add("id_friend", Integer.toString(friendId))
                .build();
        Request request = new Request.Builder()
                .url(FRIEND_DELETE)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        String string = response.body().string();
        Log.d(TAG, "deleteFriend: " + string);
        int error = JsonUtil.decodeErrorFronJson(string);
        if (error==0){
            return true;
        }else return false;
    }

    /**
     * 发送消息
     *
     * @param mainUserId
     * @param friendId
     * @param info
     * @throws IOException
     */
    public static boolean postSendInfoToFriend(int mainUserId, int friendId, String info) throws IOException {
        boolean result = false;
        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id", Integer.toString(mainUserId))
                .addFormDataPart("id_friend", Integer.toString(friendId))
                .addFormDataPart("information", info)
                .build();
        Request request = new Request.Builder()
                .url(SEND_MSG)
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            try {
                int error = decodeErrorFronJson(response.body().string());
                if (error == 0) {
                    result = true;
                }
            } catch (JSONException je) {
                je.printStackTrace();
            }
        } else throw new IOException("Unexpected code " + response);
        return result;
    }


    /**
     * 发送图片
     *
     * @param mainUserId
     * @param friendId
     * @param picture
     * @throws IOException
     */
    public static void postSendImgToFriend(int mainUserId, int friendId, Picture picture) throws IOException {
        String imagePath = picture.getImagePath();
        File file = new File(imagePath);
        RequestBody image = RequestBody.create(MediaType.parse("image/jpg"), file);
        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id", Integer.toString(mainUserId))
                .addFormDataPart("id_friend", Integer.toString(friendId))
                .addFormDataPart("image", imagePath, image)
                .build();
        Request request = new Request.Builder()
                .url(SEND_MSG)
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        String string = response.body().string();
        Log.d(TAG, "sendInfoToFriend: " + string);
    }

    /**
     * 接受消息
     *
     * @param user
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static List<Information> postRecieveInfoFromFriend(User user) throws IOException, JSONException {
        int recieve_id = user.getUser_id();
        RequestBody formBody = new FormBody.Builder()
                .add("id", Integer.toString(recieve_id))
                .build();
        Request request = new Request.Builder()
                .url(REFRESH_MSG)
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        String string = response.body().string();
        List<Information> informationList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(string);
        JSONObject infoObject = jsonObject.getJSONObject("information");
        JSONArray infoArray = infoObject.getJSONArray("information");
        int num = infoObject.getInt("num");
        for (int i = 0; i < num; i++) {
            Information information = null;
            JSONObject info = (JSONObject) infoArray.get(i);
            int post_id = info.getInt("friendID");
            if (!info.isNull("information")) {
                information = new Information(post_id, recieve_id, info.getString("information"));
            } else if (!info.isNull("image")) {
                String jsonImageString = info.getString("image");
                Bitmap bitmap = JsonUtil.decodeImgFromImgString(jsonImageString);
                information = new Information(post_id, recieve_id, bitmap);
            } else {
                Log.e(TAG, "recieveInfoFromFriend: 包含其他类型的消息");
            }
            informationList.add(information);
        }

        return informationList;
    }

    /**
     * 人脸颜值得分
     *
     * @param user
     * @param imgPath
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static List<Face> postFaceYanZhiImage(User user, String imgPath) throws IOException, JSONException {
        int id = user.getUser_id();

        File file = new File(imgPath);
        RequestBody image = RequestBody.create(MediaType.parse("image/jpg"), file);
        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id", Integer.toString(id))
                .addFormDataPart("image", imgPath, image)
                .build();
        Request request = new Request.Builder()
                .url(FACE_ATTRIBUTE)
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        String string = response.body().string();
        Log.d(TAG, "postFaceYanZhiImage: " + string);

        List<Face> faceList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(string);
        JSONArray faceArray = jsonObject.getJSONArray("face");
        int num = jsonObject.getInt("num");   //数量
        for (int i = 0; i < num; i++) {
            JSONObject faceObject = (JSONObject) faceArray.get(i);

            JSONArray positionArray = faceObject.getJSONArray("xywh");
            List<Integer> position = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                position.add((Integer) positionArray.get(j));
            }
            String attractive = faceObject.getString("attractive");  //是否好看
            String bread = faceObject.getString("bread");       //是否有胡子
            String chubby = faceObject.getString("chubby");       //是否圆润
            String eyeglasses = faceObject.getString("eyeglasses");   //是否戴眼镜
            String hair_color = faceObject.getString("hair_color");   //头发颜色
            String hair_shape = faceObject.getString("hair_shape");   //发型
            String hat = faceObject.getString("hat");          //是否戴帽子
            String makeup = faceObject.getString("makeup");       //是否化妆
            String sex = faceObject.getString("sex");          //性别
            String attra_score = faceObject.getString("attra_score");   //颜值的分
            Face face = new Face(position, attractive, bread, chubby, eyeglasses,
                    hair_color, hair_shape, hat, makeup, sex, attra_score);
            faceList.add(face);
        }
        return faceList;
    }

    /**
     * 人脸表情
     *
     * @param user
     * @param imgPath
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static List<Expression> postFaceBiaoQingImage(User user, String imgPath) throws IOException, JSONException {
        int id = user.getUser_id();

        File file = new File(imgPath);
        RequestBody image = RequestBody.create(MediaType.parse("image/jpg"), file);
        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id", Integer.toString(id))
                .addFormDataPart("image", imgPath, image)
                .build();
        Request request = new Request.Builder()
                .url(FACE_EXPRESSION)
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        String string = response.body().string();
        Log.d(TAG, "postFaceBiaoQingImage: " + string);

        List<Expression> expressionList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(string);
        JSONArray faceArray = jsonObject.getJSONArray("expression");
        int num = jsonObject.getInt("num");   //数量
        for (int i = 0; i < num; i++) {
            JSONObject faceObject = (JSONObject) faceArray.get(i);
            JSONArray positionArray = faceObject.getJSONArray("xywh");
            List<Integer> position = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                position.add((Integer) positionArray.get(j));
            }
            String expression = faceObject.getString("expression");
            Expression exp = new Expression(position, expression);
            expressionList.add(exp);
        }
        return expressionList;
    }


    /**
     * @param id
     */
    public static User getUserInfoY(final int id) {
        final User[] user = {null};
        //发起请求
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final Request request = new Request.Builder()
                        .url(StaticConstant.GET_ACCOUNT_INFO + "?id=" + id)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        user[0] = JsonUtil.decodeUserFronJson(response.body().string(), id);
                        response.body().close();
                    } else {
                        throw new IOException("Unexpected code:" + response);
                    }
                } catch (IOException ie) {
                    ie.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return user[0];
    }

    /**
     * 根据email获取id, 需要等待线程结束（可自行更改）
     *
     * @param email
     */
    public static int getIdByEmailY(final String email) {
        final int[] id = {-1};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(GET_ID + "?email=" + email)
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (response.code() == 200) {
                        String result = response.body().string();
                        JSONObject jObject1 = new JSONObject(result);
                        id[0] = jObject1.getInt("id");
                        Log.d(TAG, "getIdByEmailY: " + result);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (response != null) {
                        response.body().close();
                    }
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return id[0];
    }

    /**
     * 根据id获取email,需要等待线程结束（可自行更改）
     *
     * @param id
     */
    public static String getEmailByIdY(final int id) {
        final String[] email = {null};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(GET_ID + "?id=" + id)
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (response.code() == 200) {
                        String result = response.body().string();
                        JSONObject jObject1 = new JSONObject(result);
                        email[0] = jObject1.getString("email");
                        Log.d(TAG, "getIdByEmailY: " + result);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (response != null) {
                        response.body().close();
                    }
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return email[0];
    }


    /**
     * 添加好友,不需要等待线程结束（可自行更改）
     *
     * @param user
     * @param email
     * @throws IOException
     * @throws JSONException
     */
    public static int postAddFriendY(final User user, final String email) {
        final int friendId = getIdByEmailY(email);
        final int[] error = {-1};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBody requestBody = new FormBody.Builder()
                        .add("id", Integer.toString(user.getUser_id()))
                        .add("id_friend", Integer.toString(friendId))
                        .build();
                Request request = new Request.Builder()
                        .url(FRIEND_ADD)
                        .post(requestBody)
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!response.isSuccessful()) try {
                    throw new IOException("Unexpected code " + response);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String string = null;
                try {
                    string = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "addFriend: " + string);
                try {
                    error[0] = decodeErrorFronJson(string);
                } catch (JSONException je) {
                    je.printStackTrace();
                }
            }
        });
        thread.start();
        return error[0];
    }


    //给服务器发消息
    public static int postSendCofMessage(final int user_id, final String content) throws Exception {
        final Integer[] ids = {null};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBody formBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("user_id", Integer.toString(user_id))
                        .addFormDataPart("describe", content)
                        .build();
                Request request = new Request.Builder()
                        .url(SEND_COF_MESSAGE)
                        .post(formBody)
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!response.isSuccessful()) try {
                    throw new IOException("Unexpected code " + response);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String string = null;
                try {
                    string = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "sendInfoToFriend: " + string);
                try {
                    ids[0] = decodeIDFromJson(string);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ids[0];
    }

    //给服务器发图片
    public static int postSendCofOneImg(final int cof_id, final int user_id, final String imgPath) throws Exception {
        File file = new File(imgPath);
        RequestBody image = RequestBody.create(MediaType.parse("image/jpg"), file);
        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id", Integer.toString(cof_id))
                .addFormDataPart("user_id", Integer.toString(user_id))
                .addFormDataPart("image", imgPath, image)
                .build();
        Request request = new Request.Builder()
                .url(SEND_COF_IMG)
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        String string = response.body().string();
        Log.d(TAG, "postFaceYanZhiImage: " + string);
        int id = decodeIDFromJson(string);
        response.close();
        return id;
    }

    public static int postSendCofImgList(final int cof_id, final int user_id, final List<Picture> pictureList) {
        final Integer[] ids = {-1};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (Picture picture : pictureList) {
                    try {
                        ids[0] = postSendCofOneImg(cof_id, user_id, picture.getImagePath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();

        return ids[0];
    }


    //获取cof
    public static List<Cof> getCofList(final int user_id, final int index) throws IOException, JSONException, ParseException {
        final List<Cof>[] cofList = new List[]{new ArrayList<>()};
        //发起请求
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(StaticConstant.READ_COF + "?user_id=" + user_id + "&" + "index=" + index)
                        .build();
                Response response = null;

                try {
                    response = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!response.isSuccessful()) try {
                    throw new IOException("Unexpected code " + response);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String string = null;
                try {
                    string = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.d(TAG, "getCofList: " + string);
                try {
                    cofList[0] = decodeCofListFromCofString(string);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return cofList[0];
    }

    //删除cof
    public static int postDeleteCof(final int cof_id, final int user_id) {
        final Integer[] errors = {null};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBody formBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("id", Integer.toString(cof_id))
                        .addFormDataPart("user_id", Integer.toString(user_id))
                        .build();
                Request request = new Request.Builder()
                        .url(DELETE_COF)
                        .post(formBody)
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!response.isSuccessful()) try {
                    throw new IOException("Unexpected code " + response);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String string = null;
                try {
                    string = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "postDeleteCof: " + string);
                try {
                    errors[0] = decodeErrorFronJson(string);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return errors[0];
    }

    //点赞
    public static int postLikeCof(final int cof_id, final int user_id) {
        final Integer[] errors = {-1};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBody formBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("id", Integer.toString(cof_id))
                        .addFormDataPart("user_id", Integer.toString(user_id))
                        .build();
                Request request = new Request.Builder()
                        .url(COF_LIKE)
                        .post(formBody)
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!response.isSuccessful()) try {
                    throw new IOException("Unexpected code " + response);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String string = null;
                try {
                    string = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "postLikeCof: " + string);
                try {
                    errors[0] = decodeErrorFronJson(string);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return errors[0];
    }

    //取消点赞
    public static int postUnLikeCof(final int cof_id, final int user_id) {
        final Integer[] errors = {null};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBody formBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("id", Integer.toString(cof_id))
                        .addFormDataPart("user_id", Integer.toString(user_id))
                        .build();
                Request request = new Request.Builder()
                        .url(COF_UNLIKE)
                        .post(formBody)
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!response.isSuccessful()) try {
                    throw new IOException("Unexpected code " + response);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String string = null;
                try {
                    string = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "postUnLikeCof: " + string);
                try {
                    errors[0] = decodeErrorFronJson(string);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return errors[0];
    }

    //评论
    public static int postReviewCof(final int cof_id, final int user_id, final String comment) {
        final Integer[] ids = {null};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBody formBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("id", Integer.toString(cof_id))
                        .addFormDataPart("user_id", Integer.toString(user_id))
                        .addFormDataPart("comment", comment)
                        .build();
                Request request = new Request.Builder()
                        .url(COF_COMMENT)
                        .post(formBody)
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!response.isSuccessful()) try {
                    throw new IOException("Unexpected code " + response);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String string = null;
                try {
                    string = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "postReviewCof: " + string);
                try {
                    ids[0] = decodeIDFromJson(string);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ids[0];
    }

    //删除评论
    public static int postDisReviewCof(final int cof_id, final int review_id) {
        final Integer[] errors = {null};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBody formBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("id", Integer.toString(cof_id))
                        .addFormDataPart("comment_id", Integer.toString(review_id))
                        .build();
                Request request = new Request.Builder()
                        .url(COF_REMOVE_COMMENT)
                        .post(formBody)
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!response.isSuccessful()) try {
                    throw new IOException("Unexpected code " + response);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String string = null;
                try {
                    string = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "postDisReviewCof: " + string);
                try {
                    errors[0] = decodeErrorFronJson(string);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return errors[0];
    }
}