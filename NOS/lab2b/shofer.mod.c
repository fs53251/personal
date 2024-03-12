#include <linux/module.h>
#define INCLUDE_VERMAGIC
#include <linux/build-salt.h>
#include <linux/elfnote-lto.h>
#include <linux/export-internal.h>
#include <linux/vermagic.h>
#include <linux/compiler.h>

BUILD_SALT;
BUILD_LTO_INFO;

MODULE_INFO(vermagic, VERMAGIC_STRING);
MODULE_INFO(name, KBUILD_MODNAME);

__visible struct module __this_module
__section(".gnu.linkonce.this_module") = {
	.name = KBUILD_MODNAME,
	.init = init_module,
#ifdef CONFIG_MODULE_UNLOAD
	.exit = cleanup_module,
#endif
	.arch = MODULE_ARCH_INIT,
};

#ifdef CONFIG_RETPOLINE
MODULE_INFO(retpoline, "Y");
#endif


static const struct modversion_info ____versions[]
__used __section("__versions") = {
	{ 0x13c49cc2, "_copy_from_user" },
	{ 0x30a80826, "__kfifo_from_user" },
	{ 0xeb233a45, "__kmalloc" },
	{ 0xbd462b55, "__kfifo_init" },
	{ 0x37a0cba, "kfree" },
	{ 0xad6d045f, "kmalloc_caches" },
	{ 0x850e6a88, "kmalloc_trace" },
	{ 0x32ab4392, "cdev_init" },
	{ 0x2dcc02c1, "cdev_add" },
	{ 0xf7fb9e48, "cdev_del" },
	{ 0xdc0e4855, "timer_delete" },
	{ 0x6091b333, "unregister_chrdev_region" },
	{ 0xe3ec2f2b, "alloc_chrdev_region" },
	{ 0xc6f46339, "init_timer_key" },
	{ 0x24d273d1, "add_timer" },
	{ 0xa648e561, "__ubsan_handle_shift_out_of_bounds" },
	{ 0x30bfc2c, "param_ops_int" },
	{ 0xbdfb6dbb, "__fentry__" },
	{ 0x5b8239ca, "__x86_return_thunk" },
	{ 0x281823c5, "__kfifo_out_peek" },
	{ 0x122c3a7e, "_printk" },
	{ 0xa19b956, "__stack_chk_fail" },
	{ 0xba8fbd64, "_raw_spin_lock" },
	{ 0xb5b54b34, "_raw_spin_unlock" },
	{ 0x15ba50a6, "jiffies" },
	{ 0xc38c83b8, "mod_timer" },
	{ 0x4578f528, "__kfifo_to_user" },
	{ 0x453e7dc, "module_layout" },
};

MODULE_INFO(depends, "");


MODULE_INFO(srcversion, "277DC7BFBE52F5DC5EA0446");
