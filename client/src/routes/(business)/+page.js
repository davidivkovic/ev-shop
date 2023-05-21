import { getAllRequests } from '$lib/api/requests'

export async function load() {
  const requests = await getAllRequests()
  return { requests }
}