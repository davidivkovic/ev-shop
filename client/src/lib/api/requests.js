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
  await fetch(`${requestsUrls}/${id}/update-status`, {
    method: 'POST',
    body: JSON.stringify({ scheduledAt, accepted: true })
  })

const rejectRequest = async (id) =>
  await fetch(`${requestsUrls}/${id}/update-status`, {
    method: 'POST',
    body: JSON.stringify({ accepted: false })
  })

const resetDiagnostics = async (id) =>
  await fetch(`${requestsUrls}/${id}/reset-diagnostics`, { method: 'POST' })

const diagnostics = async (id, measurement) => {
  const response = await fetch(
    `${requestsUrls}/${id}/diagnostics?${new URLSearchParams({ measurement }).toString()}`,
    {
      method: 'POST'
    }
  )
  return await response.json()
}

const simulation = async (id, command) => {
  const response = await fetch(
    `${requestsUrls}/${id}/sim?${new URLSearchParams({ command }).toString()}`,
    {
      method: 'POST'
    }
  )
  if (command === 'status') return await response.json()
}

export {
  getAllProblems,
  getAllRequests,
  acceptRequest,
  rejectRequest,
  resetDiagnostics,
  diagnostics,
  simulation
}
