package com.zhaoweihao.architechturesample.util;

public class Constant {

    /**
     * 切换广州和本地服务器
     */
    static boolean isGz = true;

    /**
     * URL默认值
     */

    /**
     * "user/modify"修改用户的信息（除了密码）
     * "user/register";注册
     * "user/modifyUserPwd" 修改密码
     * "course/query?teacherId=" 查看教师的上传的课程Query
     * "course/querySelectByStuId?stuId=" 查看学生已经选择的课程QuerySelect
     * "leave/submit"提交请假条
     * course/querySelectByCourseId"通过课程的id查询选这门课的学生有哪些"
     * "noti/queryCourseNotiByCourseId";通过课程的id查询这门课的通知
     * "noti/sendCourseNoti"发送通知 post参数
     * "leave/query" 查询获取请假条或者请假信息（根据学生或老师判断）
     * "user/query?studentId=" 通过学生的id查询学生的请假信息(stuid,即数据库中的id)
     * "leave/confirm" 教师审批学生的请假信息
     * "vote/query?courseId="通过课程的id来查询投票的列表
     * "vote/add" 教师添加投票
     * "vote/record/read"学生阅读并投票
     * "course/query" 后面接上教师的id或者课程的名称来查询并选择课程
     * "discuss/comment/query?discussId="通过讨论话题的id来查询讨论
     * "discuss/comment/add"添加评论
     * "discuss/query?courseId=" 通过课程的id查询讨论话题的列表
     * "discuss/delete";删除讨论的话题
     * "discuss/add";添加讨论的话题
     * "quiz/add";添加随机点名
     * "seat/record/query?classCode="通过课程的密钥来查询选座记录
     * "seat/create";教师创建点名房间
     * "quiz/query?courseId="通过课程的id查询答题的排行
     * "course/select"通过post课程的密码选课
     * "course/submit";提交课程post课程参数
     * "course/delete" 教师删除课程
     * "user/getAvatar"获取头像
     * "user/modifyAvatar"修改头像
     * "unit/add_unit" 添加单元
     * unit/get_unit 获取单元
     * unit/delete_unit/:unit_id 删除单元
     * "unit/update_unit/:unit_id" 修改单元
     * "chapter/get_chapter" 获取章节列表
     * "chapter/add_chapter" 添加章节列表
     * "chapter/delete_chapter"删除章节
     * "chapter/update_chapter"更新章节
     * "homework/system/add_notebook_item" 添加笔记
     * "homework/system/get_notebook_items" 查询笔记
     * "activity/post_activity" 添加活动
     * "activity/get_acitivties" 获取所有活动
     * "homework/system/get_share_notebook_by_name" 搜索笔记
     * "homework/system/get_share_notebook?is_shared=true" 搜索所有共享笔记
     * "homework/system/get_share_notebook_by_tag?tag=" 搜索所有共享笔记
     * "homework/system/add_notebook_like" 添加点赞
     * "homework/system/delete_notobook_item?id=" 删除单条笔记
     * "resources/query?class_id=" 查找资源
     * "resources/submit" 提交资源
     * "resources/delete?id=" 删除资源
     * "friends/send_friend_request?"添加好友
     * "friends/get_all_to_requests?username="获取所有给我的请求
     * "friends/get_all_friends?username="获取所有添加的朋友
     * "friends/get_all_from_requests?username="获取我发出的请求
     * "friends/set_request_status/"设置状态同意或者拒接好友
     * "course/get_random_course?token="获取随机所有课程
     * "user/get_user_by_id?id="通过id和token获取用户信息
     * "user/get_user_by_username?username=tea&token="通过username和token获取用户信息
     * "homework/system/add_notebook_comment"添加笔记评论
     * "homework/system/get_notebook_comments?notebook_id="获取笔记评论
     * "user/reset_password"重置密码
     */

//    public static String BASE_URL = "https://test.tanxinkui.cn/";
    public static String BASE_URL = "https://test.tanxinkui.cn/";

    public static final String BASE_NO_SOLIDI_URL = "https://test.tanxinkui.cn";
    public static final String UPLOAD_SERVER_FILE_URL = "https://test.tanxinkui.cn/api/stuffs/upload";
    public static final String LOGIN_URL = "user/login";
    public static final String REGISTER_URL = "user/register";
    public static final String MODIFY_PROFILE_URL = "user/modify";
    public static final String MODIFY_USER_PASSWORD_URL = "user/modifyUserPwd";
    public static final String QUERY_COURSE_BY_TEACHER_ID_URL = "course/query?teacherId=";
    public static final String QUERY_COURSE_BY_STUDENT_ID_URL = "course/querySelectByStuId?stuId=";
    public static final String QUERY_SELECT_COURSE_STUDENT_BY_COURSE_ID_URL = "course/querySelectByCourseId";
    public static final String QUERY_COURSE_NOTI_BY_COURSE_ID_URL = "noti/queryCourseNotiByCourseId";
    public static final String SEND_COURSE_NOTI_URL = "noti/sendCourseNoti";
    public static final String QUERY_LEAVE_URL = "leave/query";
    public static final String QUERY_LEAVE_BY_STUDENT_ID_URL = "user/query?studentId=";
    public static final String CONFIRM_LEAVE_URL = "leave/confirm";
    public static final String LEAVE_SUBMIT_URL = "leave/submit";
    public static final String QUERY_VOTE_BY_COURSE_ID_URL = "vote/query?courseId=";
    public static final String ADD_VOTE_URL = "vote/add";
    public static final String ADD_TEST_URL = "test/add";
    public static final String QUERY_TEST_BY_CHAPTER_ID_URL = "test/query?chapter_id=";
    public static final String READ_AND_VOTE_URL = "vote/record/read";
    public static final String ADD_AND_TEST_URL = "test/record/read";
    public static final String QUERY_AND_SELECT_COURSE_URL = "course/query";
    public static final String QUERY_COMMENT_BY_DISCUSS_ID_URL = "discuss/comment/query?discussId=";
    public static final String ADD_COMMENT_URL = "discuss/comment/add";
    public static final String QUERY_DISCUSS_BY_COURSE_ID_URL = "discuss/query?courseId=";
    public static final String DELETE_DISCUSS_URL = "discuss/delete";
    public static final String ADD_DISCUSS_URL = "discuss/add";
    public static final String ADD_QUIZ_URL = "quiz/add";
    public static final String QUERY_SEAT_RECORD_BY_CLASS_CODE_URL = "seat/record/query?classCode=";
    public static final String CREATE_SEAT_URL = "seat/create";
    public static final String QUERY_QUIZ_RANK_BY_COURSE_ID_URL = "quiz/query?courseId=";
    public static final String SELECT_COURSE_URL = "course/select";
    public static final String SUBMIT_COURSE_URL = "course/submit";
    public static final String DELETE_COURSE_URL = "course/delete";
    public static final String GET_AVATAR_URL = "user/getAvatar";
    public static final String MODIFY_AVATAR_URL = "user/modifyAvatar";
    public static final String ADD_UNIT_URL = "unit/add_unit";
    public static final String GET_UNIT_URL = "unit/get_unit";
    public static final String DELETE_UNIT_URL = "unit/delete_unit/";
    public static final String UPDATE_UNIT_URL = "unit/update_unit/";
    public static final String GET_CHAPTER_URL = "chapter/get_chapter";
    public static final String ADD_CHAPTER_URL = "chapter/add_chapter";
    public static final String UPDATE_CHAPTER_URL = "chapter/update_chapter";
    public static final String DELETE_CHAPTER_URL = "chapter/delete_chapter";
    public static final String ADD_NOTE_URL = "homework/system/add_notebook_item";
    public static final String GET_PERSONAL_ALL_NOTE_URL = "homework/system/get_notebook_items?user_id=";
    public static final String POST_ACTIVITY_URL = "activity/post_activity";
    public static final String GET_ACTIVITIES_URL = "activity/get_acitivties";
    public static final String GET_SHARE_NOTEBOOK_URL = "homework/system/get_share_notebook_by_name";
    public static final String GET_ALL_SHARE_NOTEBOOK_URL = "homework/system/get_share_notebook?is_shared=true";
    public static final String GET_NOTEBOOK_BY_TAG = "homework/system/get_share_notebook_by_tag?tag=";
    public static final String ADD_LIKE_NOTEBOOK_URL = "homework/system/add_notebook_like";
    public static final String DELETE_NOTEBOOK_URL = "homework/system/delete_notobook_item?id=";
    public static final String QERRY_RESOURCE_URL = "resources/query?class_id=";
    public static final String DELETE_RESOURCE_URL = "resources/delete?id=";
    public static final String SUBMIT_RESOURCE_URL = "resources/submit";
    public static final String SEND_FRIEND_REQUEST_URL = "friends/send_friend_request?";
    public static final String GET_ALL_FRIENDS_TO_ME_REQUEST_URL = "friends/get_all_to_requests?username=";
    public static final String GET_ALL_FROM_ME_REQUEST_URL = "friends/get_all_from_requests?username=";
    public static final String GET_MY_ALL_FRIENDS_URL = "friends/get_all_friends?username=";
    public static final String SET_REQUEST_STATUS_URL = "friends/set_request_status/";
    public static final String GET_RANDOM_COURSE_URL = "course/get_random_course?token=";
    public static final String GET_USERINFO_BY_ID_AND_TOKEN_URL = "user/get_user_by_id?id=";
    public static final String GET_USERINFO_BY_USERNAME_AND_TOKEN_URL = "user/get_user_by_username?username=";
    public static final String ADD_NOTEBOOK_COMMENT_URL = "homework/system/add_notebook_comment";
    public static final String GET_NOTEBOOK_COMMENT_BY_ID_URL = "homework/system/get_notebook_comments?notebook_id=";
    public static final String RESRT_PASSWORD_URL = "user/reset_password";


    /**
     * ws聊天服务器
     */
    public static final String CHAT_WS_SERVER = "ws://139.199.87.26:8887";


    public static final String COURSE_ID = "course_id";
    public static final String RECEIVER_ID = "receiver_id";
    public static final String IS_FRIEND_TALK = "is_friend_talk";
    /**
     * 成功返回值200，新的失败值401,之前的失败值500
     */
    public static int SUCCESS_CODE = 200;
    public static int FAIL_CODE = 401;
    public static int FAIL_ORIGIN_CODE = 500;
    /**
     * 调试模式 关闭/开启
     */
    public static final boolean DEBUG = true;

    static {
        if (!isGz) {
//            BASE_URL = "http://192.168.1.43:9001/";
            BASE_URL = "http://172.20.10.4:9001/";
        }
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }


}
