SUMMARY = "Xen Argo Linux module headers."
DESCRIPTION = "Argo UAPI available to user-land programs to implement Argo \
communications."
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

require argo-version.inc

S = "${WORKDIR}/git/argo-linux"

inherit allarch

do_configure[noexec] = "1"

do_compile[noexec] = "1"

do_install() {
    oe_runmake INSTALL_HDR_PATH=${D}${prefix} headers_install
}
