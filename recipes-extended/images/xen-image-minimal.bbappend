IMAGE_INSTALL += " \
    libargo \
    argo-linux-module \
"

# These enable booting the image with Argo enabled
WKS_FILE:qemux86-64 = "qemuboot-xen-argo-x86-64.wks"
WKS_FILES:qemux86-64:append = " qemuboot-xen-argo-x86-64.wks"
