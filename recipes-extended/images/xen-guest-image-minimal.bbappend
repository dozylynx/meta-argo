IMAGE_INSTALL += " \
    libargo \
    argo-linux-module \
"

do_testimage[depends] += " xen-guest-image-minimal:do_build"
