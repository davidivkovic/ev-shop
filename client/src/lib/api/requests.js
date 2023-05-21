import { baseUrl, fetch } from '.'

const requestsUrls = baseUrl + '/repair-requests'

const getAllProblems = async () => {
  const response = await fetch(`${requestsUrls}/problems`)
  return response.json()
}

const getAllRequests = async () => {
  const response = await fetch(requestsUrls)
  return await response.json()
}

export { getAllProblems, getAllRequests }
