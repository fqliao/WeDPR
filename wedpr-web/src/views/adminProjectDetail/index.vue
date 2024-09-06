<template>
  <div class="create-data">
    <div class="con">
      <div class="title-radius">项目详情</div>
    </div>
    <div class="info-container">
      <div class="whole">
        <div class="half">
          <span class="title">项目名称：</span>
          <span class="info" :title="dataInfo.name"> {{ dataInfo.name }} </span>
        </div>
      </div>
      <div class="whole">
        <div class="half">
          <span class="title">项目简介：</span>
          <span class="info" :title="dataInfo.projectDesc"> {{ dataInfo.projectDesc }} </span>
        </div>
      </div>
      <div class="whole">
        <div class="half">
          <span class="title">创建模式：</span>
          <span class="info"> {{ mode[dataInfo.type] }} </span>
        </div>
      </div>
    </div>
    <div class="con">
      <div class="title-radius">项目内任务</div>
    </div>
    <div class="form-search">
      <el-form :inline="true" @submit="queryHandle" :model="searchForm" ref="searchForm" size="small">
        <el-form-item prop="ownerAgency" label="创建机构：">
          <el-select clearable size="small" style="width: 160px" v-model="searchForm.ownerAgencyName" placeholder="请选择">
            <el-option :key="item" v-for="item in agencyList" multiple :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <!-- <el-form-item prop="ownerAgencyName" label="创建部门：">
          <el-select clearable size="small" style="width: 160px" v-model="searchForm.ownerAgencyName" placeholder="请选择">
            <el-option :key="item" v-for="item in agencyList" multiple :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item> -->
        <el-form-item prop="owner" label="创建用户：">
          <el-select clearable size="small" style="width: 160px" v-model="searchForm.owner" placeholder="请选择">
            <el-option :key="item" v-for="item in agencyList" multiple :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="name" label="任务名称：">
          <el-input style="width: 160px" placeholder="请输入" v-model="searchForm.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="createTime" label="创建时间：">
          <el-date-picker style="width: 160px" v-model="searchForm.createTime" type="date" placeholder="请选择日期"> </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="queryFlag" @click="queryHandle">
            {{ queryFlag ? '查询中...' : '查询' }}
          </el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="default" :loading="queryFlag" @click="reset"> 重置 </el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="tableContent autoTableWrap" style="min-height: 400px; position: relative">
      <el-table v-if="total" :max-height="tableHeight" size="small" v-loading="loadingFlag" :data="tableData" :border="true" class="table-wrap">
        <el-table-column label="任务模板" prop="jobType">
          <template v-slot="scope"> <img class="type-img" :src="handleData(scope.row.jobType).src" /> {{ handleData(scope.row.jobType).label }} </template>
        </el-table-column>
        <el-table-column label="任务ID" prop="id" />
        <el-table-column label="创建时间" prop="createTime" />
        <el-table-column label="发起机构" prop="ownerAgency" />
        <el-table-column label="参与机构" prop="createTime" />
        <el-table-column label="任务状态" prop="status">
          <template v-slot="scope">
            <el-tag size="small" v-if="scope.row.status === 'RunSuccess'" effect="dark" color="#52B81F">成功</el-tag>
            <el-tag size="small" v-else-if="scope.row.status == 'RunFailed'" effect="dark" color="#FF4D4F">失败</el-tag>
            <el-tag size="small" v-else-if="scope.row.status === 'Running'" effect="dark" color="#3071F2">运行中</el-tag>
            <el-tag size="small" v-else effect="dark" color="#3071F2">{{ jobStatusMap[scope.row.status] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template v-slot="scope">
            <el-button size="small" @click="goDetail(scope.row.id)" type="text">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      <we-pagination :total="total" :page_offset="pageData.page_offset" :page_size="pageData.page_size" @paginationChange="paginationHandle"></we-pagination>
      <el-empty v-if="!total" :image-size="120" description="暂无数据">
        <img slot="image" src="~Assets/images/pic_empty_news.png" alt="" />
      </el-empty>
    </div>
  </div>
</template>
<script>
import { projectManageServer, jobManageServer, settingManageServer } from 'Api'
import { tableHeightHandle } from 'Mixin/tableHeightHandle.js'
import { jobStatusList, jobStatusMap } from 'Utils/constant.js'
import { mapGetters } from 'vuex'
import wePagination from '@/components/wePagination.vue'
import { handleParamsValid } from 'Utils/index.js'
export default {
  name: 'projectDetail',
  mixins: [tableHeightHandle],
  components: {
    wePagination
  },
  data() {
    return {
      searchForm: {
        ownerAgency: '',
        owner: '',
        name: '',
        createTime: ''
      },
      searchQuery: {
        ownerAgency: '',
        owner: '',
        name: '',
        createTime: ''
      },
      dataInfo: {},
      pageData: {
        page_offset: 1,
        page_size: 5
      },
      tableData: [],
      total: -1,
      mode: {
        Expert: '向导模式',
        Wizard: '专家模式'
      },
      typeList: [],
      jobStatusList,
      jobStatusMap,
      pageMode: process.env.VUE_APP_MODE
    }
  },
  created() {
    const { projectId } = this.$route.query
    this.projectId = projectId
    projectId && this.queryProject()
    this.getConfig()
  },
  computed: {
    ...mapGetters(['algList'])
  },
  methods: {
    handleData(key) {
      const data = this.algList.filter((v) => v.value === key)
      return data[0] || {}
    },
    goDetail(id) {
      this.$router.push({ path: '/jobDetail', query: { id } })
    },
    reset() {
      this.$refs.searchForm.resetFields()
    },
    async getConfig() {
      const res = await settingManageServer.getConfig({ key: 'wedpr_algorithm_templates' })
      console.log(res)
      if (res.code === 0 && res.data) {
        const realData = JSON.parse(res.data)
        console.log(realData)
        this.typeList = realData.templates.map((V) => {
          return {
            label: V.title,
            value: V.name
          }
        })
      }
    },

    // 获取项目详情
    async queryProject() {
      this.loadingFlag = true
      const { projectId } = this
      const res = await projectManageServer.queryProject({ project: { id: projectId }, onlyMeta: false })
      this.loadingFlag = false
      console.log(res)
      if (res.code === 0 && res.data) {
        const { dataList = [] } = res.data
        this.dataInfo = dataList[0] || {}
        this.queryJobByCondition()
      } else {
        this.dataInfo = {}
      }
    },
    handleParamsValid(params) {
      const validParams = {}
      Object.keys(params).forEach((key) => {
        if (!(params[key] === undefined || params[key] === null || params[key] === '')) {
          validParams[key] = params[key]
        }
      })
      return validParams
    },
    // 查询
    queryHandle() {
      this.$refs.searchForm.validate((valid) => {
        if (valid) {
          this.searchQuery = { ...this.searchForm }
          this.pageData.page_offset = 1
          this.queryJobByCondition()
        } else {
          return false
        }
      })
    },
    // 获取任务列表
    async queryJobByCondition() {
      this.loadingFlag = true
      const { name: projectName } = this.dataInfo
      const { page_offset, page_size } = this.pageData
      const { ownerAgency, owner, name, createTime } = this.searchForm
      const params = handleParamsValid({ ownerAgency, owner, name })
      if (createTime && createTime.length) {
        params.startTime = createTime[0]
        params.endTime = createTime[1]
      }
      const res = await jobManageServer.adminQueryJobByCondition({ job: { id: '', projectName, ...params }, pageNum: page_offset, pageSize: page_size })
      this.loadingFlag = false
      console.log(res)
      if (res.code === 0 && res.data) {
        const { jobs = [], total } = res.data
        this.tableData = jobs
        this.total = total
      } else {
        this.tableData = []
        this.total = 0
      }
    },
    // 分页切换
    paginationHandle(pageData) {
      console.log(pageData, 'pagData')
      this.pageData = { ...pageData }
      this.queryJobByCondition()
    }
  }
}
</script>
<style lang="less" scoped>
.type-img {
  width: 42px;
  height: auto;
  vertical-align: middle;
  margin-right: 10px;
}
div.con {
  .el-button {
    float: right;
  }
}
div.whole {
  display: flex;
  margin-bottom: 16px;
}
div.half {
  width: 50%;
  display: flex;
}
div.info-container {
  margin-bottom: 44px;
  span {
    font-size: 14px;
    line-height: 22px;
    color: #525660;
  }
  span.title {
    float: left;
    width: 86px;
    text-align: right;
    color: #525660;
  }
  span.info {
    flex: 1;
    color: #262a32;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }
  .el-row {
    margin-bottom: 16px;
  }
  span.right {
    width: 118px;
  }
}
.sub-con {
  margin-top: 32px;
  padding-left: 86px;
}
span.info {
  color: #262a32;
}
.tableContent {
  ::v-deep .el-tag {
    padding: 0 12px;
    border: none;
    line-height: 24px;
  }
}
</style>
