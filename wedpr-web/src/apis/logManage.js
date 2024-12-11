import http from '../utils/http'

// 查询明细
const getLogList = (params) => http.get('/logList', params)
// 查询明细
const queryRecordSyncStatus = (params) => http.get('/admin/queryRecordSyncStatus', params)

export default { getLogList, queryRecordSyncStatus }
