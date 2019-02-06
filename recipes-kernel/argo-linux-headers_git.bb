SUMMARY = "Xen Argo kernel headers"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

PV = "0+git${SRCPV}"

SRC_URI = "https://github.com/dozylynx/meta-argo-linux;branch=rocko"
#SRCREV = "${AUTOREV}"
SRCREV = "f58254231ce618ed77c21676178dd71ddb11dd1c"
SRC_URI[md5sum] = "2c2a94168ceca463f6c60326d881f9be"
SRC_URI[sha256sum] = "a09fe06ba79eb52a2a11c05d71afdf23a335d6a4c0265c96814669ffc827dcda"


S = "${WORKDIR}/git/src/argo-linux"

do_configure() {
:
}

do_compile() {
:
}

do_install(){
    install -d ${D}${includedir}/linux
    install ${S}/linux/argo_dev.h ${D}${includedir}/linux/
}
