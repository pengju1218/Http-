1.get的用法
AsyncHttpClient client = new AsyncHttpClient();
RequestParams params = new RequestParams();
client.get(url, params, new StringHttpResponseHandler() {
       @Override
       public void onSuccess(String content) {
            

                super.onSuccess(content);
        }

});
