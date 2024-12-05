<template>
  <el-container class="layout-contaioner-feed">
    <el-header height="64px"><HeaderTop /></el-header>
    <el-container class="main-container">
      <div class="feed">
        <div class="img-con">
          <img src="~Assets/images/feed.png" />
        </div>
        <div class="info-con">
          <h3>欢迎反馈</h3>
          <p>请填写您的信息，提交反馈后扫码进群</p>
          <el-form label-position="top" :inline="false" :rules="rules" :model="dataForm" ref="dataForm" size="small">
            <formCard title="">
              <el-form-item label-width="96px" label="岗位" prop="career">
                <div>
                  <el-radio-group v-model="dataForm.career">
                    <div style="margin-bottom: 12px">
                      <el-radio :label="0">学生</el-radio>
                      <el-radio :label="1">产品</el-radio>
                      <el-radio :label="1">项目管理</el-radio>
                      <el-radio :label="1">商务</el-radio>
                      <el-radio :label="1">研发</el-radio>
                      <el-radio :label="1">管理者</el-radio>
                    </div>
                    <el-radio :label="1">其他</el-radio>
                    <el-input style="width: 480px" placeholder="请输入" v-model="dataForm.career" autocomplete="off"></el-input>
                  </el-radio-group>
                </div>
              </el-form-item>
            </formCard>
            <formCard title="">
              <el-form-item label-width="96px" label="所在行业" prop="industry">
                <el-radio-group v-model="dataForm.industry">
                  <div style="margin-bottom: 12px">
                    <el-radio :label="0">政务</el-radio>
                    <el-radio :label="1">医疗</el-radio>
                    <el-radio :label="1">金融</el-radio>
                    <el-radio :label="1">互联网</el-radio>
                  </div>
                  <el-radio :label="1">其他</el-radio>
                  <el-input style="width: 480px" placeholder="请输入" v-model="dataForm.industry" autocomplete="off"></el-input>
                </el-radio-group>
              </el-form-item>
            </formCard>
            <formCard title="">
              <el-form-item label-width="96px" label="所在城市" prop="city">
                <el-cascader style="width: 480px" :options="options" v-model="dataForm.city"></el-cascader>
              </el-form-item>
            </formCard>
            <formCard title="">
              <el-form-item label-width="96px" label="熟悉程度：" prop="deep">
                <el-radio-group v-model="dataForm.deep">
                  <el-radio :label="0">完全不懂</el-radio>
                  <el-radio :label="1">已在应用</el-radio>
                  <el-radio :label="1">从业者</el-radio>
                  <el-radio :label="1">非常熟悉</el-radio>
                </el-radio-group>
              </el-form-item>
            </formCard>
            <formCard title="">
              <el-form-item label-width="96px" label="来访目的" prop="aim">
                <el-radio-group v-model="dataForm.aim">
                  <el-radio :label="0">随便看看</el-radio>
                  <el-radio :label="1">使用</el-radio>
                  <el-radio :label="1">学习</el-radio>
                  <el-radio :label="1">有应用需求</el-radio>
                  <el-radio :label="1">商务合作</el-radio>
                </el-radio-group>
              </el-form-item>
            </formCard>
            <div class="sub-con">
              <el-button type="primary">提交反馈</el-button>
            </div>
          </el-form>
        </div>
      </div>
    </el-container>
  </el-container>
</template>

<script>
import HeaderTop from '../layout/headerTop'
import { mapGetters } from 'vuex'
import formCard from '@/components/formCard.vue'
import { regionData } from 'element-china-area-data'

export default {
  components: {
    HeaderTop,
    formCard
  },
  data() {
    return {
      dataForm: {
        career: '',
        industry: '',
        city: '',
        deep: '',
        aim: ''
      },
      rules: {
        career: [{ required: true, message: '项目名称不能为空', trigger: 'blur' }],
        industry: [{ required: true, message: '数据集不能为空', trigger: 'blur' }],
        city: [{ required: true, message: '模式不能为空', trigger: 'blur' }],
        deep: [{ required: true, message: '模式不能为空', trigger: 'blur' }],
        aim: [{ required: true, message: '模式不能为空', trigger: 'blur' }]
      },
      options: regionData
    }
  },
  computed: {
    ...mapGetters(['permission', 'bread', 'userinfo'])
  },
  created() {
    console.log(regionData)
  },
  methods: {
    feed() {
      this.$router.push({ path: 'feed' })
    },
    closeFeed() {
      this.hiddenFeed = true
    }
  }
}
</script>
<style lang="less" scoped>
.layout-contaioner-feed {
  height: 100%;
  .el-aside {
    padding: 0 10px;
    box-sizing: border-box;
  }
  .main-container {
    height: 100%;
    overflow: auto;
  }

  .elmain {
    height: 100%;
    padding: 0;
    padding-bottom: 24px;
    padding-right: 24px;
    background-color: #f6f8fc;
    .elmain-container {
      padding: 30px 0;
      background-color: white;
      border-radius: 24px;
      overflow-y: auto;
      height: 100%;
      min-width: 960px;
      overflow-x: auto;
      .bread-con {
        height: 50px;
        line-height: 20px;
        padding-left: 20px;
      }
      div.scroll {
        height: calc(100% - 50px);
        overflow-y: auto;
        padding: 0 20px;
        box-sizing: border-box;
      }
    }
  }
  .el-menu {
    border-right: none;
  }
  .el-header {
    padding: 0;
  }
  .main-container {
    background-color: rgb(246, 248, 252);
    padding-bottom: 54px;
    display: block;
  }
  .feed {
    width: 800px;
    margin: 0 auto;
    .img-con {
      img {
        width: 100%;
        height: auto;
        display: block;
      }
    }
    .info-con {
      padding: 32px 40px 60px 40px;
      background-color: white;
      h3 {
        font-size: 24px;
        line-height: 34px;
        font-weight: 600;
        margin-bottom: 6px;
      }
      p {
        font-size: 14px;
        line-height: 22px;
        color: #787b84;
        margin-bottom: 20px;
      }
      ::v-deep .el-form-item__label {
        color: #262a32;
        font-size: 16px;
        line-height: 24px;
        display: block;
        padding-bottom: 14px;
      }
      ::v-deep .el-radio-group {
        padding-left: 10px;
      }
      div.form-card {
        padding: 24px;
        padding-bottom: 6px;
        margin-bottom: 20px;
        margin-top: 0;
      }
    }
    .sub-con {
      text-align: center;
      margin-top: 10px;
      .el-button {
        width: 300px;
      }
    }
  }
}
</style>
