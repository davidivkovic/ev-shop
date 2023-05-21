import { baseUrl, fetch } from '.'

const repairsUrl = baseUrl + '/repair-requests'

const getAllProblems = async () => {
  const response = await fetch(`${repairsUrl}/problems`)
  return response.json()
}

export { getAllProblems }
