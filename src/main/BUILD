android_library(
    name = "ThorCoreML",
    srcs = glob(["java/**/*.java"]),
    custom_package = "org.ftc9974.thorcore.ml",
    assets = [
        "//tensorflow/contrib/lite/java/demo/app/src/main/assets:labels_mobilenet_quant_v1_224.txt",
        "@tflite_mobilenet//:mobilenet_quant_v1_224.tflite",
    ],
    assets_dir = "",
    manifest = "AndroidManifest.xml",
    resource_files = glob(["res/**"]),
    tags = ["manual"],
    deps = [
        "//$TENSORFLOW_HOME/contrib/lite/java:tensorflowlite",
        "//$TENSORFLOW_HOME/contrib/lite/java/src/testhelper/java/org/tensorflow/lite:testhelper",
        "@androidsdk//com.android.support:support-v13-25.2.0",
        "@androidsdk//com.android.support:support-v4-25.2.0",
    ],
)