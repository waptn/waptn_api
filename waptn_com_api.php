<?php
//配置信息开始
$iiiLabVideoDownloadURL = "http://www.waptn.com/api/index.php";   //短视频解析接口地址
$clientId = "58d4259475f52a55";   //分配的客户ID,用户后台http://www.waptn.com/openapi.php 查看
$clientSecretKey = "4e426e4d1de2475763057c8383fb62a2";  //分配的客户密钥,用户后台http://www.waptn.com/openapi.php 查看
//配置信息结束

//必要的参数
$link = "http://v.XXXX.com/hq8aoA/";  //视频分享地址
$timestamp = time() * 1000;   //时间戳
$sign = md5($link . $timestamp . $clientSecretKey); //签名


function file_get_contents_post($url, $post) {
    $options = array(
            "http"=> array(
                "method"=>"POST",
                "header" => "Content-type: application/x-www-form-urlencoded",
                "content"=> http_build_query($post)
            ),
    );
    $result = file_get_contents($url,false, stream_context_create($options));
    return $result;
}

$data = file_get_contents_post($iiiLabVideoDownloadURL, array("link" => $link, "timestamp" => $timestamp, "sign" => $sign, "clientId" => $clientId));
$data_arr = json_decode(trim($data), true);
print_r($data_arr); 
