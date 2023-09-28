SUMMARY = "Xen Argo kernel headers"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

require argo-version.inc

S = "${WORKDIR}/git/src/argo-linux"

do_configure[noexec] = "1"

do_compile[noexec] = "1"

do_install(){
    install -d ${D}${includedir}/linux
    install ${S}/linux/argo_dev.h ${D}${includedir}/linux/
}
