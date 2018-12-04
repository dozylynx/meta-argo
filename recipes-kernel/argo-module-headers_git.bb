SUMMARY = "Xen Argo Linux module headers."
DESCRIPTION = "Argo UAPI available to user-land programs to implement Argo \
communications."
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

PV = "git${SRCPV}"

SRC_URI = "git://github.com/dozylynx/meta-argo-linux;branch=rocko"
#SRCREV = "${AUTOREV}"
SRCREV = "93a81b59a0ff4378deae15f1557346ebde69cd00"
SRC_URI[md5sum] = "2c2a94168ceca463f6c60326d881f9be"
SRC_URI[sha256sum] = "a09fe06ba79eb52a2a11c05d71afdf23a335d6a4c0265c96814669ffc827dcda"

S = "${WORKDIR}/git/src/argo-linux"

inherit allarch

do_install() {
    oe_runmake INSTALL_HDR_PATH=${D}${prefix} headers_install
}

# Skip build steps.
do_compile[noexec] = "1"
do_configure[noexec] = "1"
