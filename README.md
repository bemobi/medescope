Medescope Android Download Library
=======
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Medescope%20Android%20Download%20Library-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/2799)

This a is a ready-to-use library that encapsulate the Android Download Manager. Using an interface you can easily connect to your Activity or Fragment and used it. It runs on other process as an independent service. 

Download
--------

Download grab via Gradle:
```groovy
    dependencies{
        compile 'br.com.bemobi:medescope:1.1.0
    }
```

Usage
--------
To download a file.

```
        Medescope
        .getInstance(this)
            .enqueue("DOWNLOAD_ID",
                "http://somefileiwanttodownload.com/file",
                "file_name",
                "Name that you appear on notification",
                "{some:'samplejson'}"
                );
```

Set an application name to be shown on notification bar.

```
        Medescope.getInstance(this).setApplicationName("My Application Name"));
```

Using a callback interface to simply response on your code.

```
        Medescope.getInstance(this).subscribeStatus(this, "DOWNLOAD_ID", new DownloadStatusCallback() {
            @Override
            public void onDownloadNotEnqueued(String downloadId) {
                //TODO DO SOMETHING
            }

            @Override
            public void onDownloadPaused(String downloadId, int reason) {
                //TODO DO SOMETHING
            }

            @Override
            public void onDownloadInProgress(String downloadId, int progress) {
                //TODO DO SOMETHING
            }

            @Override
            public void onDownloadOnFinishedWithError(String downloadId, int reason, String data) {
                //TODO DO SOMETHING
            }

            @Override
            public void onDownloadOnFinishedWithSuccess(String downloadId, String filePath, String data) {
                //TODO DO SOMETHING
            }

            @Override
            public void onDownloadCancelled(String downloadId) {
                //TODO DO SOMETHING
            }
        });
```

For more, check our sample.

Contributing
--------

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

License
--------

    Copyright 2015 Bemobi

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
