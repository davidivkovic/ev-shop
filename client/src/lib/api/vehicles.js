import { baseUrl, fetch } from '.'

const vehicleUrl = baseUrl + '/vehicles'

const addVehicle = async ({ make, model, registration }) =>
  fetch(`${vehicleUrl}/add`, {
    method: 'POST',
    body: JSON.stringify({ make, model, registration })
  })

const requestRepair = async ({ problem, shop }) => {
  fetch(`${vehicleUrl}/request-repair?` + new URLSearchParams({ problem, shop }).toString(), {
    method: 'POST'
  })
}

export { addVehicle, requestRepair }
