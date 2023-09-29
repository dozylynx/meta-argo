IMAGE_INSTALL += " \
    libargo \
    argo-linux-module \
"

# These enable booting the image with Argo enabled
WKS_FILE:qemux86-64 = "qemuboot-xen-argo-x86-64.wks"
WKS_FILES:qemux86-64:append = " qemuboot-xen-argo-x86-64.wks"

do_testimage[depends] += " xen-image-minimal:do_build"

# The default wic script for x86 depends on deployed xen
XEN_IMAGE_WIC_DEPENDS = ""
XEN_IMAGE_WIC_DEPENDS:x86-64 = "xen:do_deploy"
do_image_wic[depends] += " ${XEN_IMAGE_WIC_DEPENDS}"
