<script>
  import XIcon from '~icons/tabler/x'
  export let title
  export let text
  export let close

</script>

<div class="mx-auto w-[900px] rounded-lg border bg-white border-zinc-300 p-6 mt-6">
  <div class="flex items-center justify-between">
    <div>
      <p class="font-bold text-[15px]">{title}</p>
      <p class="text-sm">{text}</p>
    </div>
    <button class="bg-white" on:click={close}>
      <XIcon />
    </button>
  </div>
</div>
