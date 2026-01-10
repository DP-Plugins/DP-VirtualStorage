<center><img src="https://i.postimg.cc/MKPVVR1s/dplogo-512.png" alt="logo"></center>
<center><img src="https://i.postimg.cc/RZ9dqPFx/introduce.png" alt="introduce"></center>

DP-VirtualStorage is a **personal virtual storage plugin** that provides each player with their own private storage space.  
Players can safely store items in a virtual inventory that is accessible anytime, anywhere.

---

<center><img src="https://i.postimg.cc/RZ9dqP08/description.png" alt="description"></center>

- Each player has a **personal virtual storage** independent of physical chests  
- Open storage anytime using a simple command  
- Supports **slot expansion** through admin configuration or coupons  
- Storage data is **automatically saved** on join/quit  
- Admins can **view or manage other players’ storages**  
- Supports **multi-language** configuration  

---

<center><img src="https://i.postimg.cc/rwcjzhpH/depend-plugin.png" alt="depend-plugin"></center>

- All DP-Plugins require the **`DPP-Core`** plugin  
- The plugin will not work if **`DPP-Core`** is not installed  
- You can download **`DPP-Core`** here: <a href="https://github.com/DP-Plugins/DPP-Core/releases" target="_blank">Click me!</a>  
- **PlaceholderAPI** is supported (optional, based on plugin.yml)  

---

<center><img src="https://i.postimg.cc/dV01RxJB/installation.png" alt="installation"></center>

1️⃣ Place the **`DPP-Core`** plugin and this plugin file (**`DP-VirtualStorage-*.jar`**) into your server’s **`plugins`** folder  

2️⃣ Restart the server, and the plugin will be automatically enabled  

3️⃣ Configuration files will be generated on first run  

---

<center><img src="https://i.postimg.cc/jSKcC85K/settings.png" alt="settings"></center>

- **`config.yml`**  
  - Default virtual storage slot size  
  - Coupon-based slot expansion settings  
  - Language selection  

- **Language files**  
  - `lang/en_US.yml`  
  - `lang/ko_KR.yml`  

---

<center><img src="https://i.postimg.cc/SxqdjZKw/command.png" alt="command"></center>

❗ Some commands require admin permission (`dpvs.admin`)

**Command List and Examples**

| Command | Permission | Description | Example |
|---|---|---|---|
| `/dpvs open` | dpvs.open | Open your virtual storage | `/dpvs open` |
| `/dpvs lookup <player>` | dpvs.admin | Open another player’s storage | `/dpvs lookup Steve` |
| `/dpvs defaultslot <size>` | dpvs.admin | Set default storage size | `/dpvs defaultslot 45` |
| `/dpvs setcoupon` | dpvs.admin | Set slot expansion coupon | `/dpvs setcoupon` |
| `/dpvs givecoupon <slot>` | dpvs.admin | Give slot expansion coupon | `/dpvs givecoupon 9` |
| `/dpvs reload` | dpvs.admin | Reload plugin | `/dpvs reload` |

**❗Notes when using commands**

- Storage contents are saved automatically  
- Coupon items permanently expand storage size  
- Admin commands require **OP** or `dpvs.admin` permission  

---

<center><img src="https://i.postimg.cc/Z5ZH0fqL/api-integration.png" alt="api-integration"></center>

- Supports **PlaceholderAPI** for dynamic text  

---

<center><a href="https://discord.gg/JnMCqkn2FX"><img src="https://i.postimg.cc/4xZPn8dC/discord.png" alt="discord"></a></center>

- https://discord.gg/JnMCqkn2FX  
- Join our Discord for support, bug reports, or feature requests  
- Feedback and improvement ideas are always welcome!

---
