<template>
  <div class="create-data">
    <div class="title-radius">资源详情</div>
    <div class="info-container">
      <div class="whole">
        <div class="half">
          <span class="title">资源ID：</span>
          <span class="info" :title="dataInfo.datasetId"> {{ dataInfo.datasetId }} </span>
        </div>
        <div class="half">
          <span class="title right">资源名称：</span>
          <span class="info" :title="dataInfo.datasetTitle"> {{ dataInfo.datasetTitle }} </span>
        </div>
      </div>
      <div class="whole">
        <div class="half">
          <span class="title">所属机构：</span>
          <span class="info" :title="dataInfo.ownerAgencyName"> {{ dataInfo.ownerAgencyName }} </span>
        </div>
        <div class="half">
          <span class="title right">所属用户：</span>
          <span class="info" :title="dataInfo.ownerUserName"> {{ dataInfo.ownerUserName }} </span>
        </div>
      </div>
      <div class="whole">
        <div class="half">
          <span class="title">样本量：</span>
          <span class="info" :title="dataInfo.recordCount"> {{ dataInfo.recordCount }} </span>
        </div>
        <div class="half">
          <span class="title right">特征量：</span>
          <span class="info" :title="dataInfo.columnCount"> {{ dataInfo.columnCount }} </span>
        </div>
      </div>
      <div class="whole">
        <div class="half">
          <span class="title">创建时间：</span>
          <span class="info" :title="dataInfo.createTcreateAtime"> {{ dataInfo.createAt }} </span>
        </div>
        <div class="half">
          <span class="title right">字段：</span>
          <span class="info" :title="dataInfo.datasetFields"> {{ dataInfo.datasetFields }} </span>
        </div>
      </div>
      <div class="whole">
        <div class="half">
          <span class="title">数据来源：</span>
          <span class="info" :title="dataInfo.dataSourceType"> {{ dataInfo.dataSourceType }} </span>
        </div>
        <div class="half">
          <span class="title right">链上存证：</span>
          <span class="info" :title="dataInfo.datasetHash"> {{ dataInfo.datasetHash }} </span>
        </div>
      </div>
    </div>
    <div class="title-radius">使用记录</div>
    <div class="tableContent autoTableWrap" v-if="total">
      <el-table :max-height="tableHeight" size="small" v-loading="loadingFlag" :data="tableData" :border="true" class="table-wrap">
        <el-table-column label="任务ID" prop="id" show-overflow-tooltip />
        <el-table-column label="所属项目" prop="projectId" show-overflow-tooltip>
          <template v-slot="scope">
            <span class="link" @click="goProjectDetail(scope.row.projectId)">{{ scope.row.projectId }}</span>
          </template>
        </el-table-column>
        <el-table-column label="发起机构" prop="ownerAgency" />
        <el-table-column label="参与机构" prop="participate" />
        <el-table-column label="发起用户" prop="owner" />
        <el-table-column label="创建时间" prop="createTime" />
        <el-table-column label="任务状态" prop="status">
          <template v-slot="scope">
            <el-tag size="small" v-if="scope.row.status === 'RunSuccess'" effect="dark" color="#52B81F">成功</el-tag>
            <el-tag size="small" v-else-if="scope.row.status == 'RunFailed'" effect="dark" color="#FF4D4F">失败</el-tag>
            <el-tag size="small" v-else-if="scope.row.status === 'Running'" effect="dark" color="#3071F2">运行中</el-tag>
            <el-tag size="small" v-else effect="dark" color="#3071F2">{{ jobStatusMap[scope.row.status] }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <we-pagination :total="total" :page_offset="pageData.page_offset" :page_size="pageData.page_size" @paginationChange="paginationHandle"></we-pagination>
    </div>
    <el-empty v-if="!total" :image-size="120" description="暂无数据">
      <img slot="image" src="~Assets/images/pic_empty_news.png" alt="" />
    </el-empty>
  </div>
</template>
<script>
import { dataManageServer, projectManageServer } from 'Api'
import { tableHeightHandle } from 'Mixin/tableHeightHandle.js'
import { mapGetters } from 'vuex'
import { jobStatusMap } from 'Utils/constant.js'
export default {
  name: 'dataDetail',
  mixins: [tableHeightHandle],
  data() {
    return {
      dataInfo: {
        permissions: {}
      },
      pageData: {
        page_offset: 1,
        page_size: 10
      },
      tableData: [],
      total: 0,
      rangeMap: {
        selfUserGroup: '本用户组内',
        selfAgency: '本机构内',
        agencyList: '指定机构',
        userList: '指定用户',
        global: '全局'
      },
      jobStatusMap
    }
  },
  created() {
    const { datasetId } = this.$route.query
    this.datasetId = datasetId
    datasetId && this.getDetail()
  },
  computed: {
    ...mapGetters(['userId', 'agencyId'])
  },
  methods: {
    goProjectDetail(projectId) {
      this.$router.push({ path: '/projectDetail', query: { projectId } })
    },
    // 获取数据集详情
    async getDetail() {
      this.loadingFlag = true
      const { datasetId } = this
      const res = await dataManageServer.adminQueryDataset({ datasetId })
      this.loadingFlag = false
      console.log(res)
      if (res.code === 0 && res.data) {
        this.dataInfo = { ...res.data, isOwner: res.data.ownerUserName === this.userId && res.data.ownerAgencyName === this.agencyId }
        this.adminQueryJobsByDatasetId()
        console.log(this.dataInfo)
      } else {
        this.dataInfo = {}
      }
    },
    // 获取数据集详情
    async adminQueryJobsByDatasetId() {
      this.loadingFlag = true
      const { datasetId } = this
      const { page_offset, page_size } = this.pageData
      const res = await projectManageServer.adminQueryJobsByDatasetId({ datasetId: datasetId, pageNum: page_offset, pageSize: page_size })
      this.loadingFlag = false
      console.log(res)
      if (res.code === 0 && res.data) {
        const { jobList, total } = res.data
        this.total = total
        this.tableData = jobList.map((v) => {
          let participate = ''
          try {
            participate = JSON.parse(v.parties).map((v) => v.agency)
            participate = Array.from(new Set(participate)).join('，')
          } catch {
            participate = ''
          }
          return {
            participate,
            ...v
          }
        })
      }
    },
    paginationHandle(pageData) {
      this.pageData = { ...pageData }
      this.adminQueryJobsByDatasetId()
    }
  }
}
</script>
<style lang="less" scoped>
div.whole {
  display: flex;
  margin-bottom: 16px;
}
div.half {
  width: 50%;
  display: flex;
}
div.all {
  width: 100%;
  display: flex;
}
span.link {
  cursor: pointer;
  color: #3071f2;
}
.el-empty {
  margin-top: 0;
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
    width: 106px;
    text-align: right;
    color: #525660;
  }
  span.info {
    flex: 1;
    color: #262a32;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    .tags-con {
      white-space: normal;
      word-break: break-all;
    }
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
