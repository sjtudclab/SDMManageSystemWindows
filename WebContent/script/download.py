#coding:utf-8

import requests
import base64

# url结构说明：
# http://Gitlab服务器地址/GitLab提供的API访问路径?private_token=xxxxxxxxxxxxx&参数名1=参数值1&参数名2=参数值2[&...参数名n=参数值n]
# 参数说明：file_path指定项目中文件的路径；ref指定分支
url = "http://202.120.40.111:8800/dclab2017/ModelManage?private_token=vV9KSSeBDVvZTe-eBmLp&file_path=dclab2017/ModelManage&ref=master"

# 获取请求该url的结果，并转换为json
result = requests.get(url)
data = result.json()

# 由于content内容是base64编码过的，所以需要先作解码处理，不然返回的是一堆字母
print base64.b64decode(data['content'])