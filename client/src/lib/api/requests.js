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

const acceptRequest = async (id, scheduledAt) =>
  await fetch(`${requestsUrls}/${id}`, {
    method: 'POST',
    body: JSON.stringify({ scheduledAt, accepted: true })
  })

const rejectRequest = async (id) =>
  await fetch(`${requestsUrls}/${id}`, {
    method: 'POST',
    body: JSON.stringify({ accepted: false })
  })

export { getAllProblems, getAllRequests, acceptRequest, rejectRequest }
