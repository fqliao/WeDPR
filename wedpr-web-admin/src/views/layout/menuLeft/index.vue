<template>
  <el-menu :default-active="defaultIndex" class="el-menu-container" :collapse-transition="true" :unique-opened="true" @select="handleSelect" :collapse="isCollapse" :router="true">
    <div v-for="menu in menuList" :key="menu.name">
      <el-sub-menu v-if="menu.subMenu" :index="menu.name">
        <template #title>
          <div class="img-con">
            <img v-if="defaultIndex === menu.name" :src="bindIcon(menu.icon_active_src)" alt="" class="icon-tab" />
            <img v-else :src="bindIcon(menu.icon_src)" alt="" class="icon-tab" />
          </div>
          <span class="menu-text">{{ menu.text }}</span>
        </template>
        <el-menu-item-group v-for="submenu in menu.subMenu" :key="submenu.name">
          <el-menu-item :key="submenu.name" :index="submenu.name">
            <template #title>
              <span>{{ submenu.text }}</span>
            </template>
          </el-menu-item>
        </el-menu-item-group>
      </el-sub-menu>
      <el-menu-item :index="menu.name" v-else>
        <div class="img-con">
          <img v-if="defaultIndex === menu.name" :src="bindIcon(menu.icon_active_src)" alt="" class="icon-tab" />
          <img v-else :src="bindIcon(menu.icon_src)" alt="" class="icon-tab" />
        </div>

        <template #title>
          <span class="menu-text">{{ menu.text }}</span>
        </template>
      </el-menu-item>
    </div>
  </el-menu>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  props: {
    isCollapse: {
      type: Boolean,
      default: false
    }
  },

  data() {
    return {
      menuList: [
        {
          name: 'home',
          path: '/home',
          text: '首页',
          icon: 'el-icon-document-copy',
          icon_src: 'home',
          icon_active_src: 'home_active'
        },
        {
          name: 'agencyManage',
          path: '/agencyManage',
          text: '机构管理',
          icon: 'el-icon-document-copy',
          icon_src: 'home',
          icon_active_src: 'home_active'
        },

        {
          name: 'dataManage',
          path: '/dataManage',
          text: '数据资源',
          icon_src: 'data',
          icon_active_src: 'data_active'
        },
        {
          name: 'projectManage',
          path: '/projectManage',
          text: '项目空间',
          icon_src: 'project',
          icon_active_src: 'project_active'
        },
        {
          name: 'logManage',
          path: '/logManage',
          text: '日志审计',
          icon_src: 'log',
          icon_active_src: 'log_active'
        },
        {
          name: 'certificateManage',
          path: '/certificateManage',
          text: '证书管理',
          icon_src: 'cert',
          icon_active_src: 'cert_active'
        }
      ],
      defaultIndex: '',
      timer: null
    }
  },
  computed: {
    ...mapGetters(['permission', 'userinfo', 'bread'])
  },
  created() {
    // 权限先过滤一遍
    this.menuList = this.menuList.filter((v) => this.permission.includes(v.name))
    if (this.bread && this.bread.length) {
      this.defaultIndex = this.bread[0].name
    }
  },
  watch: {
    defaultIndex(n) {
      console.log(n)
    },
    bread(v) {
      if (v && v.length) {
        this.defaultIndex = v[0].name
      }
    },
    permission() {
      // 权限先过滤一遍
      this.menuList = this.menuList.filter((v) => this.permission.includes(v.name))
      if (this.bread && this.bread.length) {
        this.defaultIndex = this.bread[0].name
      }
    }
  },
  methods: {
    bindIcon(src) {
      return require('../../../assets/images/' + src + '.png')
    },
    handleSelect(index) {
      this.defaultIndex = index
      console.log(this.defaultIndex)
    }
  },
  destroyed() {
    this.timer && clearInterval(this.timer)
  }
}
</script>

<style lang="less">
.el-aside {
  height: 100%;
  width: 100%;
  background-color: #f6f8fc;
  text-align: left;
  color: white;
  border-radius: 4px;
  position: relative;
  span.menu-text {
    padding-left: 12px;
  }
  .img-con {
    padding: 10px 12px;
    border-radius: 4px;
  }
  .el-menu--collapse {
    width: auto;
  }
  .el-tooltip {
    padding: 0 0 !important;
    display: flex !important;
    align-items: center;
  }
  .el-menu-item {
    height: 40px;
    font-weight: 500;
    padding-left: 0px !important;
    padding-right: 0px;
    margin-top: 5px;
    margin-bottom: 5px;
    display: flex;
    align-items: center;
    line-height: 1;
    color: #787b84;
  }
  .el-menu-item:hover,
  .el-menu-item.is-active {
    background: #e8f0ff;
    color: #3071f2;
  }
  .el-menu-item:hover {
    color: #787b84;
  }
  .el-menu-item {
    color: #787b84;
  }
}
.el-aside.aside-menu {
  height: 100%;
  width: 100%;
}
.el-menu-container.el-menu {
  background-color: #f6f8fc;
  color: #fff;
  font-size: 14px;
}
.icon-tab {
  width: 20px;
  height: 20px;
}
span.todo {
  position: absolute;
  height: 16px;
  padding: 0 5.26px;
  border-radius: 8px;
  background-color: #ff5f4a;
  right: 3px;
  top: 3px;
  text-align: center;
  color: white;
  line-height: 16px;
  font-size: 10px;
}
span.after-title {
  position: static;
  margin-left: 6px;
}
</style>
