<@override name="body">
<div class="container-fluid">

        <form role="form" action="/qrcode/" method="post">
            <div class="form-group">

                <label for="exampleInputEmail1">
                    二维码地址
                </label>
                <input class="form-control" id="exampleInputEmail1" name="url"/>

                <p class="help-block">
                    输入你想生成二维码的地址
                </p>
                <div class="checkbox">

                    <label>
                        <input type="checkbox"/> 确认提交
                    </label>
                </div>
                <button type="submit" class="btn btn-primary" onclick="this.submit()">
                    提交
                </button>
            </div>
        </form>
</div>
</@override>
<@extends name="base.ftl"/>