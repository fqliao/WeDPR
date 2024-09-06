<template>
  <el-dialog width="462px" title="申请调用" @close="handleClose" :visible="showApplyModal">
    <div class="form-con">
      <el-form label-position="right" size="small" :model="dataForm" :rules="dataFormRules" ref="dataForm" :label-width="formLabelWidth">
        <el-form-item label="绑定AccessKey：" prop="accessKey">
          <el-select style="width: 280px" v-model="dataForm.accessKey" placeholder="请选择该服务所绑定的AccessKey" clearable>
            <el-option :label="item.label" :value="item.value" :key="item.label" v-for="item in accessKeyList"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div class="key-info" v-if="selectedAccessKey">
        <ul>
          <li>AccessID：</li>
          <li>{{ accessInfo.accessKeyID }}</li>
        </ul>
        <ul>
          <li>Access Secret：</li>
          <li>{{ accessInfo.accessKeySecret }}</li>
        </ul>
      </div>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button :loading="loading" type="primary" @click="handleOk">确 定</el-button>
    </div>
  </el-dialog>
</template>
<script>
import { accountManageServer, accessKeyManageServer } from 'Api'
export default {
  name: 'applyService',
  props: {
    showApplyModal: {
      type: Boolean,
      default: false
    },
    serviceId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      dataForm: {
        accessKey: ''
      },
      dataFormRules: {
        accessKey: [{ required: true, message: '绑定AccessKey不能为空', trigger: 'blur' }]
      },
      formLabelWidth: '142px',
      loading: false,
      accessKeyList: [],
      accessInfo: {},
      selectedAccessKey: '',
      pageData: {
        page_offset: 1,
        page_size: 999
      }
    }
  },
  created() {
    this.queryAccessKeyList()
  },
  methods: {
    handleClose() {
      this.$emit('closeModal')
    },
    // 获取账户列表
    async queryAccessKeyList() {
      const { page_offset, page_size } = this.pageData
      const params = { condition: { status: 'Enable', id: '' }, pageNum: page_offset, pageSize: page_size }
      const res = await accessKeyManageServer.queryAccessKeyList(params)
      console.log(res)
      if (res.code === 0 && res.data) {
        const { credentials = [] } = res.data
        this.accessKeyList = credentials.map((v) => {
          return {
            label: v.accessKeyID,
            value: v.accessKeyID
          }
        })
      }
    },
    // 创建账号
    async addUser(params) {
      const res = await accountManageServer.addUser(params)
      this.loading = false
      if (res.code === 0) {
        this.userNameSelectList = []
        this.$message({ type: 'success', message: '用户添加成功' })
        this.$emit('handlOK')
      }
    },
    handleOk() {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.selectedAccessKey = this.dataForm.accessKey
        }
      })
    }
  }
}
</script>
<style lang="less" scoped>
div.form-con {
  .el-upload-list__item-name {
    text-align: left;
  }
  div.type-con {
    margin-bottom: 18px;
    .type-label {
      width: 110px;
      display: inline-block;
      text-align: right;
      margin-right: 12px;
    }
  }
  .key-info {
    background-color: rgb(246, 247, 251);
    padding: 20px 10px;
    ul {
      display: flex;

      li:first-child {
        text-align: right;
        width: 120px;
      }
      li:last-child {
        flex: 1;
      }
    }
    ul:first-child {
      margin-bottom: 10px;
    }
  }
}
</style>
