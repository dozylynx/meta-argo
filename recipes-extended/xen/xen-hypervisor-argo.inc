FILESEXTRAPATHS:prepend := "${THISDIR}/xen:"

SRC_URI:append = " \
    file://argo.cfg \
    "

check_argo_is_enabled_config() {
    if ! grep -q '^CONFIG_ARGO=y$' ${S}/xen/.config ; then
        bbfatal 'Failed to enable Argo'
    fi
}

do_compile:prepend() {
    check_argo_is_enabled_config
}

do_compile:append() {
    if ! grep -q ' do_argo_op$' ${B}/xen/xen-syms.map ; then
        bbfatal 'Failed to find Argo hypercall op symbol'
    fi
}

do_install:prepend() {
    check_argo_is_enabled_config
}

do_deploy:prepend() {
    check_argo_is_enabled_config
}
