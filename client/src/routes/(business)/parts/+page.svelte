<script>
  import { setAlarmQuantity } from '$lib/api/shops.js'
  import { user } from '$lib/stores/userStore.js'
  import { get } from 'svelte/store'

  export let data

  $: shopId = get(user).shop.id

  const quantityArray = Array.from({ length: 20 }, (_, i) => i + 1)

  const changeAlarmQuantity = (e, make, type) => {
    setAlarmQuantity(shopId, make, type, e.target.value)
  }
</script>

<div>
  <h1 class="text-xl">Available parts</h1>
  <p class="text-sm">
    View all your available parts here. You can change the alarm quanity for each part.
  </p>
</div>

<table class="mt-5 table-auto w-full text-sm" >
  <thead class="border-b">
    <tr class="text-left">
      <th>Part make</th>
      <th>Part name</th>
      <th>Price</th>
      <th>Quantity</th>
      <th>Alarm quantity</th>
    </tr>
  </thead>
  <tbody>
    {#each data.parts as part}
      <tr>
        <td class="py-3">{part.make}</td>
        <td class="py-3">{part.name}</td>
        <td>${part.price}</td>
        <td>{Math.max(0, part.quantity)}</td>
        <td class="w-32">
          <select on:change={(e) => changeAlarmQuantity(e, part.make, part.type)} name="alarm-quantity" class="text-sm">
            {#each quantityArray as quantity}
              <option value={quantity} class="text-sm" selected={part.alarmQuantity === quantity}>{quantity}</option>
            {/each}
          </select>
        </td>
      </tr>
    {/each}
    
    </tbody>

  </table>
