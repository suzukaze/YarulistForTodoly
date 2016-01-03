FROM gfx2015/android:latest

MAINTAINER Jun Hiroe <Jun.Hiroe@gmail.com>

ENV PROJECT /project

RUN mkdir $PROJECT
WORKDIR $PROJECT

ADD . $PROJECT

RUN echo "sdk.dir=$ANDROID_HOME" > local.properties && \
    ./gradlew --stacktrace app:dependencies

CMD ./gradlew --stacktrace check
