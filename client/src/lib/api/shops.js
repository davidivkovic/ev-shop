import { baseUrl, fetch } from '.'

const shopsUrl = baseUrl + '/repair-shops'

const addShop = async ({ name, address, brands }) =>
  fetch(`${shopsUrl}/add`, {
    method: 'POST',
    body: JSON.stringify({ name, address, brands })
  })

const getShops = async (brand) => {
  const response = await fetch(`${shopsUrl}?${new URLSearchParams({ brand }).toString()}`)
  return response.json()
}

const getAllParts = async (id) => {
  const response = await fetch(`${shopsUrl}/${id}/parts`)
  return response.json()
}

const setAlarmQuantity = (id, make, part, quantity) => {
  fetch(
    `${shopsUrl}/${id}/parts/alarm-quantity?${new URLSearchParams({
      make,
      part,
      quantity
    }).toString()}`,
    {
      method: 'POST'
    }
  )
}

export { addShop, getShops, getAllParts, setAlarmQuantity }
