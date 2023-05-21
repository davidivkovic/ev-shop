<script>
  import { acceptRequest, rejectRequest } from '$lib/api/requests'

  export let close
  export let requestId

  let processForm

  const accept = async () => {
    const formData = new FormData(processForm)
    const scheduledAt = formData.get('scheduledAt')
    await acceptRequest(requestId, scheduledAt)
    close()
  }

  const reject = async () => {
    await rejectRequest(requestId)
    close()
  }
</script>

<form bind:this={processForm} on:submit|preventDefault={accept} class="w-[450px] p-2">
  <h3>Process the repair</h3>
  <div class="mt-5">
    <label for="date"> Date and time of the repairment </label>
    <input required class="mt-1" type="datetime-local" min={new Date().toISOString().slice(0, 16)} name="scheduledAt" />
  </div>
  <div class="mt-8 flex justify-end gap-2">
    <button on:click={reject} class="secondary" type="button">Reject</button>
    <button class="primary" type="submit">Accept</button>
  </div>
</form>
