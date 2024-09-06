import http from '../utils/http'

// get link
const getJupterLink = (params) => http.get('/jupyter/query', params)

// jupyter open
const openJupter = (params) => http.get('/jupyter/open', params)

export default {
  getJupterLink,
  openJupter
}
